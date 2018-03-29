package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository{
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";

        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );

                    statement.setLong(1, timeEntry.getProjectId());
                    statement.setLong(2, timeEntry.getUserId());
                    statement.setDate(3, Date.valueOf(timeEntry.getDate()));
                    statement.setInt(4, timeEntry.getHours());

                    return statement;
        }, key);

        return find(key.getKey().longValue());
    }

    @Override
    public TimeEntry find(long id) {
        String sql = "SELECT * FROM time_entries WHERE id=?";
        return jdbcTemplate.query(sql, new Object[]{id}, extractor);
    }

    @Override
    public List<TimeEntry> list() {
        String sql = "SELECT * FROM time_entries";
        List<TimeEntry> results = jdbcTemplate.query(sql, mapper);
        return results;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql = "UPDATE time_entries SET project_id=?, user_id=?, date=?, hours=? WHERE id=?";
        jdbcTemplate.update(
                sql,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id
        );
        return find(id);
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM time_entries WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    //rs - ResultSet
    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor = (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
