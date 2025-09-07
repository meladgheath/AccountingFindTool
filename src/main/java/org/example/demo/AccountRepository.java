package org.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountRepository {


    public Account getInfo(String account , Connection conn) throws SQLException, ClassNotFoundException {

        Statement stat = conn.createStatement();
        String sql = "select ac.branch_code as brncode , ac.cust_ac_no as ac_cust , br.branch_name brn_name, ac.iban_ac_no ac_iban , ac.AC_DESC as ac_descs from  FCUBSPRD.sttms_cust_account ac ,FCUBSPRD.sttm_branch br where\n" +
                " ac.branch_code = br.branch_code and ac.cust_ac_no = '"+account+"'" ;

        ResultSet resultSet = stat.executeQuery(sql);

        Account p = null ;
        if (resultSet.next()) {
            p = new Account();
            p.setAccount(resultSet.getString("ac_cust"));
//            p.setBrn(resultSet.getString("brncode"));
            p.setBrn(resultSet.getString("brn_name"));
            p.setIban(resultSet.getString("ac_iban"));
            p.setName(resultSet.getString("ac_descs"));
        }
        return p ;
    }
}
