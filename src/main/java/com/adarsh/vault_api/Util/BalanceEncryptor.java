package com.adarsh.vault_api.Util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Converter
public class BalanceEncryptor implements AttributeConverter<BigDecimal, String> {

    private static EncryptionUtil encryptionUtil;

    @Autowired
    public void setEncryptionUtil(EncryptionUtil util) {
        BalanceEncryptor.encryptionUtil = util;
    }

    @Override
    public String convertToDatabaseColumn(BigDecimal balance) {
        return balance == null ? null : encryptionUtil.encrypt(balance.toString());
    }

    @Override
    public BigDecimal convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new BigDecimal(encryptionUtil.decrypt(dbData));
    }
}