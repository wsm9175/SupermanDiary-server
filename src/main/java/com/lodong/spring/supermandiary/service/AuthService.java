package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.address.ConstructorAddress;
import com.lodong.spring.supermandiary.domain.admin.Invite;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.constructor.Product;
import com.lodong.spring.supermandiary.domain.file.FileList;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.dto.address.AddressDTO;
import com.lodong.spring.supermandiary.dto.auth.ConstructorInfoDTO;
import com.lodong.spring.supermandiary.dto.auth.ConstructorProductDTO;
import com.lodong.spring.supermandiary.dto.auth.DuplicateCheckDTO;
import com.lodong.spring.supermandiary.dto.auth.ProductInfoDTO;
import com.lodong.spring.supermandiary.dto.jwt.TokenRequestDTO;
import com.lodong.spring.supermandiary.jwt.TokenInfo;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.repo.*;
import com.lodong.spring.supermandiary.repo.address.ConstructorAddressRepository;
import com.lodong.spring.supermandiary.service.address.AddressService;
import com.lodong.spring.supermandiary.service.file.SaveFileService;
import edu.emory.mathcs.backport.java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserConstructorRepository userConstructorRepository;
    private final ConstructorRepository constructorRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final AffiliatedInfoRepository affiliatedInfoRepository;
    private final ConstructorProductRepository constructorProductRepository;

    private final UserConstructorTechRepository userConstructorTechRepository;
    private final UserCustomerRepository userCustomerRepository;
    private final ProductRepository productRepository;
    private final InviteRepository inviteRepository;
    private final SaveFileService saveFileService;
    private final AddressService addressService;
    private final ConstructorAddressRepository constructorAddressRepository;

    @Transactional
    public void register(UserConstructor user/*, List<UserConstructorTech> userConstructorTechList*/, AffiliatedInfo affiliatedInfo) throws IllegalStateException, Exception {
        if (isDuplicated(user.getEmail())) throw new IllegalStateException("이메일 중복값 존재");
        try {
            userConstructorRepository.save(user);
            //userConstructorTechRepository.saveAll(userConstructorTechList);
            affiliatedInfoRepository.save(affiliatedInfo);
        }catch (NullPointerException nullPointerException){
            System.out.println(nullPointerException.getMessage());
            throw new NullPointerException("빈 값 존재");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("데이터 베이스 저장 실패");
        }
        //채팅관련 유저 등록 코드 포함 예정 using savedUser
    }

    @Transactional
    public void registerConstructor(UserConstructor userConstructor, Constructor constructor, FileList fileList,
                                    MultipartFile file, AddressDTO addressDTO, List<ConstructorProductDTO> constructorProductDTOS,
                                    AffiliatedInfo affiliatedInfo) throws IllegalStateException, Exception {
        if (isDuplicated(constructor.getId())) throw new IllegalStateException();

        try {
            String address = addressDTO.getAddress();
            String addressDetail = addressDTO.getAddressDetail();
            //String[] words = address.split(" ");
            //StringBuilder sigg = new StringBuilder();
            //시도-시군구-읍면-도로명-건물번호
       /* for(int i=1;i<=2;i++){
            if(words[i].charAt(words[i].length()-1) == '시' || words[i].charAt(words[i].length()-1) == '군'
                    || words[i].charAt(words[i].length()-1) == '구'){
                sigg.append(i == 1 ? words[i] + " " : words[i]);
            }
        }*/
            //log.info(sigg.toString());
            //SiggAreas siggAreas = siggAreasRepository.findByName(sigg.toString()).orElseThrow();
            //int siggCode = siggAreas.getSidoAreas().getCode();
            ConstructorAddress constructorAddress = ConstructorAddress.builder()
                    .constructorId(constructor.getId())
                    .address(address)
                    .addressDetail(addressDetail)
                    .build();
            List<ConstructorProduct> constructorProducts = new ArrayList<>();
            //사업자 보유 기술 세팅
            for (ConstructorProductDTO constructorProductDTO : constructorProductDTOS ) {
                Product product = productRepository.findById(constructorProductDTO.getId()).orElseThrow(()->new NullPointerException("존재하지 않는 상품입니다."));
                ConstructorProduct constructorProduct = ConstructorProduct.builder()
                        .id(UUID.randomUUID().toString())
                        .constructor(constructor)
                        .product(product)
                        .isFirst(true)
                        .build();

                constructorProducts.add(constructorProduct);
            }
            userConstructorRepository.save(userConstructor);
            constructorRepository.save(constructor);
            //saveFileService.saveBusinessLicense(fileList, constructor.getId(), file);
            constructorAddressRepository.save(constructorAddress);
            constructorProductRepository.saveAll(constructorProducts);
            affiliatedInfoRepository.save(affiliatedInfo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new Exception("데이터 베이스 저장 실패");
        }
    }

    @Transactional
    public TokenInfo auth(UserConstructor user) {
        log.info("UsernamePasswordAuthenticationToken");
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword());
        log.info("Authentication");
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("TokenInfo");
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        log.info("return");
        return tokenInfo;
    }


    @Transactional
    public TokenInfo reissue(TokenRequestDTO tokenRequestDTO) throws ValidationException, NullPointerException {
        // 만료된 refresh token 에러
        if(!jwtTokenProvider.validateToken(tokenRequestDTO.getRefreshToken())){
            throw new ValidationException("유효기간지남");
        }

        // AccessToken 에서 UserName(pk) 가져오기
        String accessToken = tokenRequestDTO.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        //user pk로 유저 검색 / repo에 저장된 refresh token이 없으면 에러
        UserConstructor userConstructor = userConstructorRepository.findById(authentication.getName())
                .orElseThrow(NullPointerException::new);

        String refreshToken = userConstructor.getRefreshToken();

        if(refreshToken == null){
            throw new NullPointerException("회원정보에 refreshToken 존재 X");
        }

        //리프레시 토큰 불일치 에러
        if(!refreshToken.equals(tokenRequestDTO.getRefreshToken())){
            throw new RuntimeException("회원정보의 refreshToken과 불일치");
        }

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        insertRefreshToken(tokenInfo.getRefreshToken(), userConstructor.getPhoneNumber());

        return tokenInfo;
    }

    public boolean isDuplicateBusinessNumber(String businessNumber){
        return constructorRepository.existsByBusinessNumber(businessNumber);
    }

    public boolean isDuplicated(String phoneNumber) {
        return userConstructorRepository.existsByPhoneNumber(phoneNumber);
    }

    public DuplicateCheckDTO isDuplicateAndInvite(String phoneNumber){
        DuplicateCheckDTO duplicateCheckDTO = new DuplicateCheckDTO();
        duplicateCheckDTO.setIsDuplicate(userConstructorRepository.existsByPhoneNumber(phoneNumber));
        inviteRepository.findByPhoneNumberAndSignComplete(phoneNumber, false).ifPresent(invites -> {
            duplicateCheckDTO.setIsInvite(true);
            List<ConstructorInfoDTO> constructorInfoDTOS = new ArrayList<>();
            for(Invite invite : invites){
                ConstructorInfoDTO constructorInfoDTO = new ConstructorInfoDTO(invite.getConstructor().getId(), invite.getConstructor().getName());
                constructorInfoDTOS.add(constructorInfoDTO);
            }
            duplicateCheckDTO.setConstructorInfoDTO(constructorInfoDTOS);
        });
        return duplicateCheckDTO;
    }

    public List<ProductInfoDTO> getProductList(){
        List<Product> products = productRepository.findAll();
        List<ProductInfoDTO> productInfoDTOS = new ArrayList<>();
        for(Product product : products){
            ProductInfoDTO productInfoDTO = new ProductInfoDTO(product.getId(), product.getName());
            productInfoDTOS.add(productInfoDTO);
        }

        return productInfoDTOS;
    }

    public void insertRefreshToken(String refreshToken, String phoneNumber) {
        userConstructorRepository.insertRefreshToken(refreshToken, phoneNumber);
    }

}
