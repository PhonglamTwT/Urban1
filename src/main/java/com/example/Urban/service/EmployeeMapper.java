package com.example.Urban.service;

import com.example.Urban.dto.AccountDTO;
import com.example.Urban.dto.EmployeeDTO;
import com.example.Urban.entity.AccountEntity;
import com.example.Urban.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

//    @Mapping(source = "employee", target = "employeeDTO")
    EmployeeDTO toEmployeeDTO(EmployeeEntity employee);
//    @Mapping(source = "account", target = "accountDTO")
    AccountDTO toAccountDTO(AccountEntity account);

    List<EmployeeDTO> toEmployeeDTOs(List<EmployeeEntity> employees);

    List<AccountDTO> toAccountDTOs(List<AccountEntity> account);
}