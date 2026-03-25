package org.example;

import java.math.BigDecimal;

public record Account(String accountId, String fullName, BigDecimal balance) {
}
