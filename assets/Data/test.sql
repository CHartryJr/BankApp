SELECT ACCOUNT_HOLDER.*,BANK_ACCOUNT.AMOUNT,ACCOUNT_TYPE.ACCOUNT_DESCIPTION From ACCOUNT_HOLDER,BANK_ACCOUNT,ACCOUNT_TYPE WHERE ACCOUNT_HOLDER.REFERENCE_ID = BANK_ACCOUNT.OWNER and ACCOUNT_TYPE.ACCOUNT_ID == BANK_ACCOUNT.ACCOUNT_TYPE;