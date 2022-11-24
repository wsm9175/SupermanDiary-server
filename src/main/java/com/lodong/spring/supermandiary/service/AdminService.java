package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.domain.admin.Invite;
import com.lodong.spring.supermandiary.domain.constructor.*;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructorTech;
import com.lodong.spring.supermandiary.domain.working.Working;
import com.lodong.spring.supermandiary.dto.admin.*;
import com.lodong.spring.supermandiary.repo.*;
import com.lodong.spring.supermandiary.responseentity.PermissionEnum;
import edu.emory.mathcs.backport.java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    @Transactional(readOnly = true)
    public WorkManageDto getWorkList(String constructorId) throws NullPointerException {
        Constructor constructor = constructorRepository
                .findWithAllById(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사가 존재하지 않습니다."));

        List<ConstructorProduct> constructorProducts = constructor.getConstructorProducts();

        WorkManageDto workManageDto = new WorkManageDto();

        List<ConstructorProductWorkDto> constructorProductWorkDtoList = new ArrayList<>();
        for (ConstructorProduct constructorProduct : constructorProducts) {
            ConstructorProductWorkDto constructorProductWorkDto = new ConstructorProductWorkDto();
            constructorProductWorkDto.setId(constructorProduct.getId());
            constructorProductWorkDto.setProductName(constructorProduct.getName());
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
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        ConstructorProduct constructorProduct = new ConstructorProduct();
        constructorProduct.setId(UUID.randomUUID().toString());
        constructorProduct.setConstructor(constructor);
        constructorProduct.setName(product.getName());


        List<ConstructorProductWorkList> constructorProductWorkLists = new ArrayList<>();
        for (ProductWorkDto productWorkDto : product.getProductWorkList()) {
            ConstructorProductWorkList constructorProductWorkList = ConstructorProductWorkList.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorProduct(constructorProduct)
                    .sequence(productWorkDto.getSequence())
                    .name(productWorkDto.getName())
                    .build();
            constructorProductWorkLists.add(constructorProductWorkList);
        }
        constructorProduct.setConstructorProductWorkLists(constructorProductWorkLists);

        constructorProductRepository.save(constructorProduct);
        //constructorProductWorkListRepository.saveAll(constructorProductWorkLists);
    }


    @Transactional
    public void inviteMember(String constructorId, InviteDto inviteDto) {
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        Invite invite = Invite.builder()
                .id(UUID.randomUUID().toString())
                .constructor(constructor)
                .name(inviteDto.getName())
                .phoneNumber(inviteDto.getPhoneNumber())
                .createAt(LocalDateTime.now())
                .signComplete(false)
                .build();

        inviteRepository.save(invite);
        /////////////// SMS 전송기능 추가 예정
    }

    @Transactional(readOnly = true)
    public WorkerManageDto getWorkerManage(String constructorId) {
        List<Invite> inviteList = inviteRepository
                .findByConstructorIdAndSignComplete(constructorId, false)
                .stream().filter(invite -> !invite.isSignComplete()).toList();

        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository
                .findByConstructorId(constructorId)
                .orElse(new ArrayList<>());

        WorkerManageDto workManageDto = new WorkerManageDto();

        List<InvitationWorkerDto> invitationWorkerDtoList = new ArrayList<>();
        List<WorkerInfoDto> workerInfoDtoList = new ArrayList<>();

        // 초대 기술자 세팅
        for (Invite invite : inviteList) {
            if (!invite.isSignComplete()) {
                InvitationWorkerDto invitationWorkerDto = new InvitationWorkerDto();
                invitationWorkerDto.setName(invite.getName());
                invitationWorkerDtoList.add(invitationWorkerDto);
            }
        }

        // 기술자 목록(이름, 전화번호, 기술목록, 활성화 여부) 세팅
        for (AffiliatedInfo affiliatedInfo : affiliatedInfos) {
            UserConstructor userConstructor = affiliatedInfo.getUserConstructor();
            WorkerInfoDto workerInfoDto = new WorkerInfoDto();
            workerInfoDto.setWorkerId(userConstructor.getId());
            workerInfoDto.setName(userConstructor.getName());
            workerInfoDto.setPhoneNumber(userConstructor.getPhoneNumber());
            List<WorkerTechDto> workerTechDtoList = new ArrayList<>();
            //작업자 기술 목록
            log.info("userConsturctor tech size : " + userConstructor.getUserConstructorTeches().size());
            userConstructor.getUserConstructorTeches()
                    .stream()
                    .map(UserConstructorTech::getTechName)
                    .forEach(s -> {
                        workerTechDtoList.add(new WorkerTechDto(s));
                    });
            workerInfoDto.setWorkerTechDtoList(workerTechDtoList);
            workerInfoDto.setIsActive(userConstructor.isActive());
            workerInfoDtoList.add(workerInfoDto);
        }

        workManageDto.setInvitationWorkerDtoList(invitationWorkerDtoList);
        workManageDto.setWorkerInfoDtoList(workerInfoDtoList);
        return workManageDto;
    }

    @Transactional
    public void controlWorkerActivation(String userId, boolean isActivate) {
        UserConstructor userConstructor = userConstructorRepository
                .findById(userId)
                .orElseThrow(()-> new NullPointerException("해당 작업자는 존재하지 않습니다."));
        userConstructor.getRoles().remove(0);
        userConstructor.getRoles().add(isActivate ? PermissionEnum.USER.name() : PermissionEnum.SUSPENDMEBER.name());
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
    public List<SalesDto> getSales(String constructorId) {
        List<Working> workings = workingRepository
                .findByConstructorIdAndCompleteConstructTrue(constructorId)
                .orElseThrow(() -> new NullPointerException("매출이 존재하지 않습니다."));

        List<SalesDto> salesDtoList = new ArrayList<>();
        workings.stream().forEach(working -> {
            SalesDto salesDto = new SalesDto();
            salesDto.setWorkId(working.getId());
            salesDto.setName(working.getConstructorProduct().getName());
            salesDto.setPrice(working.getEstimate().getPrice());
            salesDto.setCompleteConstructDate(working.getCompleteConstructDate());
            salesDto.setPay(working.isCompletePay());
            salesDto.setCompletePayDate(working.getCompletePayDate());
            salesDtoList.add(salesDto);
        });

        return salesDtoList;
    }
}
