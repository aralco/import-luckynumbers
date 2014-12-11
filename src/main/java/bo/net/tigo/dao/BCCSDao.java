package bo.net.tigo.dao;

import bo.net.tigo.model.InAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aralco on 11/18/14.
 */
@Repository
public class BCCSDao {

    private static final Logger logger = LoggerFactory.getLogger(BCCSDao.class);

    private static final String SUCURSAL = "sucursal";
    private static final String NRO_DESDE = "nro_desde";
    private static final String NRO_HASTA = "nro_hasta";
    private static final String NROCUENTA = "nrocuenta";
    private static final String MENSAJE = "mensaje";
    private static final String FROZEN_NUMBERS = "frozenNumbers";
    private static final String FREE_NUMBERS = "freeNumbers";
    private static final String UNLOCKED_NUMBERS = "unlockedNumbers";
    private static final String LOCKED_NUMBERS = "lockedNumbers";
    private static final String RESERVED_NUMBERS = "reservedNumbers";
    private SimpleJdbcCall frozenNumbersProc;
    private SimpleJdbcCall freeNumbersProc;
    private SimpleJdbcCall reserveNumberProc;
    private SimpleJdbcCall unlockNumbersProc;
    private SimpleJdbcCall lockedNumbersProc;
    private SimpleJdbcCall reservedNumbersProc;

    @Value("${storedprocedure.bccs.frozennumbers.name}")
    private String frozenNumbersProcName;
    @Value("${storedprocedure.bccs.freenumbers.name}")
    private String freeNumbersProcName;
    @Value("${storedprocedure.bccs.reservenumber.name}")
    private String reserveNumberProcName;
    @Value("${storedprocedure.bccs.unlocknumbers.name}")
    private String unlockNumbersProcName;
    @Value("${storedprocedure.bccs.lockednumbers.name}")
    private String lockedNumbersProcName;
    @Value("${storedprocedure.bccs.reservednumbers.name}")
    private String reservedNumbersProcName;

    @Autowired
    public void setDataSource(@Qualifier("as400DataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        this.frozenNumbersProc = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(frozenNumbersProcName)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(SUCURSAL, Types.NUMERIC),
                        new SqlParameter(NRO_DESDE, Types.VARCHAR),
                        new SqlParameter(NRO_HASTA, Types.VARCHAR)
                )
                .returningResultSet(FROZEN_NUMBERS, new RowMapper<InAudit>() {
                    @Override
                    public InAudit mapRow(ResultSet resultSet, int i) throws SQLException {
                        InAudit inAudit = new InAudit();
                        inAudit.setRow(resultSet.getString(1));
                        return inAudit;
                    }
                });

        this.freeNumbersProc = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(freeNumbersProcName)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                    new SqlParameter(SUCURSAL, Types.NUMERIC),
                    new SqlParameter(NRO_DESDE, Types.VARCHAR),
                    new SqlParameter(NRO_HASTA, Types.VARCHAR)
                )
                .returningResultSet(FREE_NUMBERS, new RowMapper<InAudit>() {
                    @Override
                    public InAudit mapRow(ResultSet resultSet, int i) throws SQLException {
                        InAudit inAudit = new InAudit();
                        inAudit.setRow(resultSet.getString(1));
                        return inAudit;
                    }
                });

        this.reserveNumberProc = new SimpleJdbcCall(jdbcTemplate)
        .withProcedureName(reserveNumberProcName)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(NROCUENTA, Types.VARCHAR),
                        new SqlOutParameter(MENSAJE, Types.VARCHAR)
                );

        this.unlockNumbersProc = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(unlockNumbersProcName)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                    new SqlParameter(SUCURSAL, Types.NUMERIC),
                    new SqlParameter(NRO_DESDE, Types.VARCHAR),
                    new SqlParameter(NRO_HASTA, Types.VARCHAR)
                )
                .returningResultSet(UNLOCKED_NUMBERS, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getString(1);
                    }
                });

        this.lockedNumbersProc = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(lockedNumbersProcName)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(SUCURSAL, Types.NUMERIC),
                        new SqlParameter(NRO_DESDE, Types.VARCHAR),
                        new SqlParameter(NRO_HASTA, Types.VARCHAR)
                )
                .returningResultSet(LOCKED_NUMBERS, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getString(1);
                    }
                });

        this.reservedNumbersProc = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(reservedNumbersProcName)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(SUCURSAL, Types.NUMERIC),
                        new SqlParameter(NRO_DESDE, Types.VARCHAR),
                        new SqlParameter(NRO_HASTA, Types.VARCHAR)
                )
                .returningResultSet(RESERVED_NUMBERS, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getString(1);
                    }
                });


    }

    @SuppressWarnings("unchecked")
    public List<InAudit> getFrozenNumbers(int city, String from, String to)    {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(SUCURSAL, city)
                .addValue(NRO_DESDE, from)
                .addValue(NRO_HASTA, to);
        Map out = frozenNumbersProc
                .execute(parameterSource);
        logger.info("getFrozenNumbers::result="+out.get(FROZEN_NUMBERS));
        return (List<InAudit>)out.get(FROZEN_NUMBERS);
    }

    @SuppressWarnings("unchecked")
    public List<InAudit> getFreeNumbers(int city, String from, String to)    {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(SUCURSAL, city)
                .addValue(NRO_DESDE, from)
                .addValue(NRO_HASTA, to);
        Map out = freeNumbersProc
                .execute(parameterSource);
        logger.info("getFreeNumbers::result="+out.get(FREE_NUMBERS));
        return (List<InAudit>)out.get(FREE_NUMBERS);
    }

    public String reserveNumber(String number)    {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(NROCUENTA, number);
        logger.info("reserveNumber::result="+number);
        Map result = reserveNumberProc.execute(parameterSource);
        return (String)result.get(MENSAJE);
    }

    @SuppressWarnings("unchecked")
    public List<String> unlockNumbers(int city, String from, String to)    {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(SUCURSAL, city)
                .addValue(NRO_DESDE, from)
                .addValue(NRO_HASTA, to);
        Map out = unlockNumbersProc
                .execute(parameterSource);
        logger.info("unlockNumbers::result="+out.get("unlockedNumbers"));
        return unWrap((List<String>) out.get(UNLOCKED_NUMBERS));
    }

    @SuppressWarnings("unchecked")
    public List<String> getLockedNumbers(int city, String from, String to)    {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(SUCURSAL, city)
                .addValue(NRO_DESDE, from)
                .addValue(NRO_HASTA, to);
        Map out = lockedNumbersProc
                .execute(parameterSource);
        logger.info("getLockedNumbers::result="+out.get(LOCKED_NUMBERS));
        return unWrap((List<String>)out.get(LOCKED_NUMBERS));
    }

    @SuppressWarnings("unchecked")
    public List<String> getReservedNumbers(int city, String from, String to)    {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(SUCURSAL, city)
                .addValue(NRO_DESDE, from)
                .addValue(NRO_HASTA, to);
        Map out = reservedNumbersProc
                .execute(parameterSource);
        logger.info("getReservedNumbers::result="+out.get(RESERVED_NUMBERS));
        return unWrap((List<String>)out.get(RESERVED_NUMBERS));
    }

    private List<String> unWrap(List<String> inputList)  {
        List<String> outputList = new ArrayList<String>(0);
        String fields[];
        for(String input : inputList) {
            fields = input.split(",");
            outputList.add(fields[0]);
        }
        return outputList;
    }

}
