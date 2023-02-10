package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.domain.admin.Invite;
import com.lodong.spring.supermandiary.domain.constructor.*;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructorTech;
import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import com.lodong.spring.supermandiary.domain.working.Working;
import com.lodong.spring.supermandiary.dto.admin.*;
import com.lodong.spring.supermandiary.dto.auth.ConstructorProductDTO;
import com.lodong.spring.supermandiary.dto.calendar.WorkerFilterDto;
import com.lodong.spring.supermandiary.enumvalue.WorkLevelEnum;
import com.lodong.spring.supermandiary.repo.*;
import com.lodong.spring.supermandiary.enumvalue.PermissionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final ConstructorProductRepository constructorProductRepository;
    private final ConstructorProductWorkListRepository constructorProductWorkListRepository;
    private final ConstructorRepository constructorRepository;
    private final InviteRepository inviteRepository;
    private final AffiliatedInfoRepository affiliatedInfoRepository;
    private final UserConstructorRepository userConstructorRepository;
    private final WorkingRepository workingRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public WorkManageDto getWorkList(String constructorId) throws NullPointerException {
        Constructor constructor = constructorRepository.findWithAllById(constructorId).orElseThrow(() -> new NullPointerException("해당 시공사가 존재하지 않습니다."));

        List<ConstructorProduct> constructorProducts = constructor.getConstructorProducts();

        WorkManageDto workManageDto = new WorkManageDto();

        List<ConstructorProductWorkDto> constructorProductWorkDtoList = new ArrayList<>();
        for (ConstructorProduct constructorProduct : constructorProducts) {
            ConstructorProductWorkDto constructorProductWorkDto = new ConstructorProductWorkDto();
            constructorProductWorkDto.setId(constructorProduct.getId());
            constructorProductWorkDto.setProductName(constructorProduct.getProduct().getName());
            List<WorkListDto> workListDtos = new ArrayList<>();
            for (ConstructorProductWorkList constructorProductWorkList : constructorProduct.getConstructorProductWorkLists()) {
                WorkListDto workListDto = new WorkListDto();
                workListDto.setId(constructorProductWorkList.getId());
                workListDto.setSequence(constructorProductWorkList.getSequence());
                workListDto.setWorkName(constructorProductWorkList.getName());
                workListDto.setFileIn(constructorProductWorkList.isFileIn());
                workListDtos.add(workListDto);
            }
            constructorProductWorkDto.setWorkList(workListDtos);
            constructorProductWorkDtoList.add(constructorProductWorkDto);
        }
        workManageDto.setConstructorProductWorkDtoList(constructorProductWorkDtoList);

        CallingNumberDto callingNumberDto = new CallingNumberDto();
        callingNumberDto.setPhoneNumber(constructor.getCallingNumber());
        callingNumberDto.setIsCertification(constructor.isCertificatePhoneNumber());
        workManageDto.setCallingNumber(callingNumberDto);

        OrderManageDto orderManageDto = new OrderManageDto();
        orderManageDto.setActivate(constructor.isOrderManage());
        orderManageDto.setOrderMethod(constructor.getOrderMethod());
        orderManageDto.setPlaceOrder(constructor.getPlaceOrder());
        workManageDto.setOrderManage(orderManageDto);

        PayManageDto payManageDto = new PayManageDto();
        payManageDto.setActivate(constructor.isPayActivation());
        payManageDto.setPayTemplate(constructor.getPayTemplate());
        payManageDto.setBank(constructor.getBank());
        payManageDto.setBankAccountNumber(constructor.getBankAccount());
        workManageDto.setPayManage(payManageDto);

        return workManageDto;
    }

    @Transactional
    public void setProduct(String constructorId, ProductDto product) {
        Constructor constructor = Constructor.builder().id(constructorId).build();
        Product selectProduct = productRepository.findById(product.getProductId()).orElseThrow(()->new NullPointerException("해당 product는 존재하지 않습니다."));
        ConstructorProduct constructorProduct = new ConstructorProduct();
        constructorProduct.setId(UUID.randomUUID().toString());
        constructorProduct.setConstructor(constructor);
        constructorProduct.setProduct(selectProduct);

        List<ConstructorProductWorkList> constructorProductWorkLists = new ArrayList<>();
        for (ProductWorkDto productWorkDto : product.getProductWorkList()) {
            ConstructorProductWorkList constructorProductWorkList = ConstructorProductWorkList.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorProduct(constructorProduct)
                    .sequence(productWorkDto.getSequence())
                    .name(productWorkDto.getName())
                    .isFileIn(productWorkDto.isFileIn())
                    .build();
            constructorProductWorkLists.add(constructorProductWorkList);
        }
        constructorProduct.setConstructorProductWorkLists(constructorProductWorkLists);
        constructorProductRepository.save(constructorProduct);
    }
    @Transactional
    public void alterProduct(ConstructorProductAlterDTO constructorProductAlterDTO) {
        ConstructorProduct constructorProduct = constructorProductRepository
                .findById(constructorProductAlterDTO.getConstructorProductId())
                .orElseThrow(()->new NullPointerException("해당 시공사 상품은 존재하지 않습니다."));
        constructorProductWorkListRepository.deleteAllByConstructorProduct(constructorProduct);

        List<ConstructorProductWorkList> constructorProductWorkLists = new ArrayList<>();

        Optional.ofNullable(constructorProductAlterDTO.getProductWorkList()).orElseGet(Collections::emptyList).forEach(productWorkDto -> {
            ConstructorProductWorkList constructorProductWorkList = ConstructorProductWorkList.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorProduct(constructorProduct)
                    .sequence(productWorkDto.getSequence())
                    .name(productWorkDto.getName())
                    .isFileIn(productWorkDto.isFileIn())
                    .build();
            constructorProductWorkLists.add(constructorProductWorkList);
        });
        constructorProduct.setConstructorProductWorkLists(constructorProductWorkLists);
    }

    @Transactional
    public void inviteMember(String constructorId, InviteDto inviteDto) throws NullPointerException{
        Constructor constructor = constructorRepository.findById(constructorId).orElseThrow(()->new NullPointerException("존재하지 않는 시공사 입니다."));

        Invite invite = Invite.builder().id(UUID.randomUUID().toString()).constructor(constructor).name(inviteDto.getName()).phoneNumber(inviteDto.getPhoneNumber()).createAt(LocalDateTime.now()).signComplete(false).build();
        inviteRepository.save(invite);
        /////////////// SMS 전송기능 추가 예정
        invitePhoneNumber(invite.getPhoneNumber(), constructor.getName());
    }

    @Transactional(readOnly = true)
    public WorkerManageDto getWorkerManage(String constructorId) {
        List<Invite> inviteList = inviteRepository.findByConstructorIdAndSignComplete(constructorId, false).stream().filter(invite -> !invite.isSignComplete()).toList();

        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository.findByConstructorId(constructorId).orElse(new ArrayList<>());

        WorkerManageDto workManageDto = new WorkerManageDto();

        List<InvitationWorkerDto> invitationWorkerDtoList = new ArrayList<>();
        List<WorkerInfoDto> workerInfoDtoList = new ArrayList<>();

        // 초대 기술자 세팅
        for (Invite invite : inviteList) {
            if (!invite.isSignComplete()) {
                InvitationWorkerDto invitationWorkerDto = new InvitationWorkerDto(invite.getName());
                invitationWorkerDtoList.add(invitationWorkerDto);
            }
        }
        // 기술자 목록(이름, 전화번호, 기술목록, 활성화 여부) 세팅
        for (AffiliatedInfo affiliatedInfo : affiliatedInfos) {
            UserConstructor userConstructor = affiliatedInfo.getUserConstructor();
            List<WorkerTechDto> workerTechDtoList = new ArrayList<>();
            //작업자 기술 목록
            log.info("userConsturctor tech size : " + userConstructor.getUserConstructorTeches().size());
            userConstructor.getUserConstructorTeches().stream().map(UserConstructorTech::getProduct).forEach(product -> {
                workerTechDtoList.add(new WorkerTechDto(product.getName()));
            });
            WorkerInfoDto workerInfoDto = new WorkerInfoDto(userConstructor.getId(), userConstructor.getName(), userConstructor.getPhoneNumber(), workerTechDtoList, userConstructor.isActive());
            workerInfoDtoList.add(workerInfoDto);
        }

        workManageDto.setInvitationWorkerDtoList(invitationWorkerDtoList);
        workManageDto.setWorkerInfoDtoList(workerInfoDtoList);
        return workManageDto;
    }

    @Transactional
    public void controlWorkerActivation(String userId, boolean isActivate) {
        UserConstructor userConstructor = userConstructorRepository.findById(userId).orElseThrow(() -> new NullPointerException("해당 작업자는 존재하지 않습니다."));
        userConstructor.getRoles().remove(0);
        userConstructor.getRoles().add(isActivate ? PermissionEnum.USER.name() : PermissionEnum.SUSPENDMEBER.name());
        userConstructor.setActive(isActivate);
        userConstructorRepository.save(userConstructor);
    }

    @Transactional
    public void activatePayManage(String constructorId, boolean isActivate) {
        constructorRepository.updatePayManage(constructorId, isActivate);
    }

    @Transactional
    public void activateOrderManage(String constructorId, boolean isActivate) {
        constructorRepository.updateOrderManage(constructorId, isActivate);
    }

    @Transactional
    public void payInfoUpdate(String constructorId, String bank, String bankAccount) {
        constructorRepository.updatePayInfo(constructorId, bank, bankAccount);
    }

    @Transactional
    public void updateCallingNumber(String constructorId, String phoneNumber) {
        constructorRepository.updateCallingNumber(constructorId, phoneNumber);
    }

    @Transactional
    public void updatePayTemplate(String constructorId, String template) {
        constructorRepository.updatePayTemplate(constructorId, template);
    }

    @Transactional(readOnly = true)
    public SaleInfoDto getSales(String constructorId) {
        List<Working> workings = workingRepository.findByConstructorIdAndCompleteConstructTrue(constructorId).orElseThrow(() -> new NullPointerException("매출이 존재하지 않습니다."));
        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository.findByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 작업 가능한 사람이 없습니다."));

        List<WorkerFilterDto> workerFilterDtos = new ArrayList<>();
        List<SalesDto> salesDtoList = new ArrayList<>();

        affiliatedInfos.forEach(affiliatedInfo -> {
            if (affiliatedInfo.getUserConstructor().isActive()) {
                WorkerFilterDto workerFilterDto = new WorkerFilterDto(affiliatedInfo.getUserConstructor().getId(),affiliatedInfo.getUserConstructor().getName());
                workerFilterDtos.add(workerFilterDto);
            }
        });

        workings.forEach(working -> {
            String constructorWorkerName = null;
            String constructorWorkerId = null;
            for (WorkDetail workDetail : working.getWorkDetails()) {
                if (workDetail.getName().equals(WorkLevelEnum.CONSTRUCT.label())) {
                    constructorWorkerName = workDetail.getUserConstructor().getName();
                    constructorWorkerId = workDetail.getUserConstructor().getId();
                    break;
                }
            }
            SalesDto salesDto = null;
            if(working.getApartment() != null){
                salesDto = new SalesDto(working.getId(), working.getConstructorProduct().getProduct().getName(),
                        working.getEstimate().getPrice(), working.getCompleteConstructDate(),
                        working.isCompletePay(), working.getCompletePayDate(), constructorWorkerId, constructorWorkerName,
                        working.getApartment().getName(), working.getApartmentDong(), working.getApartmentHosu());
            }else if(working.getOtherHome() != null){
                salesDto = new SalesDto(working.getId(), working.getConstructorProduct().getProduct().getName(),
                        working.getEstimate().getPrice(), working.getCompleteConstructDate(),
                        working.isCompletePay(), working.getCompletePayDate(), constructorWorkerId, constructorWorkerName,
                        working.getOtherHome().getName(), working.getOtherHomeDong(), working.getOtherHomeHosu());
            }
            salesDtoList.add(salesDto);
        });
        SaleInfoDto saleInfoDto = new SaleInfoDto(workerFilterDtos, salesDtoList);

        return saleInfoDto;
    }

    @Transactional(readOnly = true)
    public List<ProductInfoDTO> getProductInfoDTOS(){
        List<Product> productList = productRepository.findAll();
        List<ProductInfoDTO> productInfoDTOS = new ArrayList<>();
        Optional.ofNullable(productList).orElseGet(Collections::emptyList).stream().forEach(product -> {
            ProductInfoDTO productInfoDTO = new ProductInfoDTO(product.getId(), product.getName(), product.getDetail());
            productInfoDTOS.add(productInfoDTO);
        });

        return productInfoDTOS;
    }

    private void invitePhoneNumber(String phoneNumber, String constructorName) {
        String api_key = "NCSXYLSJMWZVUBCW";
        String api_secret = "M4WHLCZXHODUNAJW2I264PUCREYUZKWX";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "01030219175");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "슈퍼맨다이어리 - " + constructorName + "에서 초대하였습니다.");
        params.put("app_version", "test app 1.2"); // application name and version
        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
