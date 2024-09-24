package com.sparta.sweethoney.domain.menu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.sparta.sweethoney.domain.common.dto.AuthUser;
import com.sparta.sweethoney.domain.common.exception.menu.NotFoundMenuException;
import com.sparta.sweethoney.domain.common.exception.menu.ProductAlreadyStoppedException;
import com.sparta.sweethoney.domain.common.exception.order.UnauthorizedAccessException;
import com.sparta.sweethoney.domain.common.exception.store.NotFoundStoreException;
import com.sparta.sweethoney.domain.common.exception.user.NotFoundUserException;
import com.sparta.sweethoney.domain.menu.dto.request.PostMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.request.PutMenuRequestDto;
import com.sparta.sweethoney.domain.menu.dto.response.DeleteMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PostMenuResponseDto;
import com.sparta.sweethoney.domain.menu.dto.response.PutMenuResponseDto;
import com.sparta.sweethoney.domain.menu.entity.Menu;
import com.sparta.sweethoney.domain.menu.entity.MenuStatus;
import com.sparta.sweethoney.domain.menu.repository.MenuRepository;
import com.sparta.sweethoney.domain.store.entity.Store;
import com.sparta.sweethoney.domain.store.repository.StoreRepository;
import com.sparta.sweethoney.domain.user.entity.User;
import com.sparta.sweethoney.domain.user.entity.UserRole;
import com.sparta.sweethoney.domain.user.entity.UserStatus;
import com.sparta.sweethoney.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    // S3
    private final AmazonS3Client s3Client;

    // S3 버킷
    @Value("${s3.bucket}")
    private String bucket;

    /* 메뉴 생성 */
    @Transactional
    public PostMenuResponseDto addMenu(
            AuthUser authUser, Long storeId, PostMenuRequestDto requestDto, MultipartFile image
    ) throws IOException {
        // 유저 확인
        User user = findUserOrElseThrow(authUser.getId());

        // 삭제된 유저인지 확인 및 권한 확인
        checkDeletedUserAndPermissions(user.getUserStatus(), user.getUserRole());

        // 가게 조회
        Store store = findStoreOrElseThrow(storeId);

        // 이미지 이름 변경
        String originalFileName = image.getOriginalFilename();
        String fileName = changeFileName(originalFileName);

        // S3에 파일을 보낼 때 파일의 종류와 크기를 알려주기
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        metadata.setContentDisposition("inline");

        // S3에 파일 업로드
        s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

        // 업로드한 파일의 S3 URL 주소
        String imageUrl = s3Client.getUrl(bucket, fileName).toString();

        // Entity 변환
        Menu menu = new Menu(requestDto, store, imageUrl);

        // DB 저장 하면서 responseDto 반환
        return new PostMenuResponseDto(menuRepository.save(menu));
    }

    /* 메뉴 수정 */
    @Transactional
    public PutMenuResponseDto updateMenu(
            AuthUser authUser, Long storeId, Long menuId, PutMenuRequestDto requestDto, MultipartFile image
    ) throws IOException {
        // 유저 확인
        User user = findUserOrElseThrow(authUser.getId());

        // 삭제된 유저인지 확인 및 권한 확인
        checkDeletedUserAndPermissions(user.getUserStatus(), user.getUserRole());

        // 가게 조회
        Store store = findStoreOrElseThrow(storeId);

        // 이미지 이름 변경
        String originalFileName = image.getOriginalFilename();
        String fileName = changeFileName(originalFileName);

        // 메뉴 조회
        Menu menu = findMenuOrElseThrow(menuId, store.getId());

        // S3에 파일을 보낼 때 파일의 종류와 크기를 알려주기
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        metadata.setContentDisposition("inline");

        // S3에 파일 업로드
        s3Client.putObject(bucket, fileName, image.getInputStream(), metadata);

        // 업로드한 파일의 S3 URL 주소
        String imageUrl = s3Client.getUrl(bucket, fileName).toString();

        // 메뉴 수정
        menu.update(requestDto, imageUrl);

        // Dto 반환
        return new PutMenuResponseDto(menu);
    }

    /* 메뉴 삭제 */
    @Transactional
    public DeleteMenuResponseDto deleteMenu(AuthUser authUser, Long storeId, Long menuId) {
        // 유저 확인
        User user = findUserOrElseThrow(authUser.getId());

        // 삭제된 유저인지 확인 및 권한 확인
        checkDeletedUserAndPermissions(user.getUserStatus(), user.getUserRole());

        // 가게 조회
        Store store = findStoreOrElseThrow(storeId);

        // 메뉴 조회
        Menu menu = findMenuOrElseThrow(menuId, store.getId());

        // 미판매인지 확인
        if (menu.getStatus() == MenuStatus.INACTIVE) {
            throw new ProductAlreadyStoppedException();
        }

        // 기존 등록된 URL 가지고 이미지 원본 이름 가져오기
        String menuImageName = extractFileNameFromUrl(menu.getImageUrl());

        // 가져온 이미지 원본 이름으로 S3 이미지 삭제
        s3Client.deleteObject(bucket, menuImageName);

        // 메뉴 삭제(미판매로 변환)
        menu.delete(MenuStatus.INACTIVE);

        // Dto 반환
        return new DeleteMenuResponseDto(menu);
    }

    /* 유저 확인 */
    private User findUserOrElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundUserException());
    }

    /* 삭제된 유저인지 확인 및 권한 확인 */
    private void checkDeletedUserAndPermissions(UserStatus status, UserRole role) {
        // 유저 삭제된 유저인지 확인
        if (status == UserStatus.DELETED) {
            throw new NotFoundUserException();
        }

        // 유저 권한 확인
        if (role == UserRole.GUEST) {
            throw new UnauthorizedAccessException();
        }
    }

    /* 가게 조회 */
    private Store findStoreOrElseThrow(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() ->
                new NotFoundStoreException());
    }

    /* 메뉴 조회 */
    private Menu findMenuOrElseThrow(Long menuId, Long storeId) {
        return menuRepository.findByIdAndStoreId(menuId, storeId).orElseThrow(() ->
                new NotFoundMenuException());
    }

    /* 이미지 파일 이름 변경 */
    private String changeFileName(String originalFileName) {
        // 이미지 등록 날짜를 붙여서 리턴
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter) + "_" + originalFileName;
    }

    /* 등록된 메뉴 기존 URL 원본 파일이름으로 디코딩 */
    private String extractFileNameFromUrl(String url) {
        try {
            // URL 마지막 슬래시의 위치를 찾아서 인코딩된 파일 이름 가져오기
            String encodedFileName = url.substring(url.lastIndexOf("/") + 1);

            // 인코딩된 파일 이름을 디코딩 해서 진짜 원본 파일 이름 가져오기
            return URLDecoder.decode(encodedFileName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // This shouldn't happen with UTF-8, but we need to handle the exception
            throw new RuntimeException("원본 파일 이름 변경 에러", e);
        }
    }
}
