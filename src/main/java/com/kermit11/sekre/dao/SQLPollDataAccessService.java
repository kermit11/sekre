 package com.kermit11.sekre.dao;

import com.kermit11.sekre.controller.PaginationInfo;
import com.kermit11.sekre.model.Author;
import com.kermit11.sekre.model.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;

@Repository("sqlPollRepo")
public class SQLPollDataAccessService implements PollDao
{
    private static final String SELECT_POLLS_COLUMNS = "SELECT polls.id AS poll_id, question, author AS author_id, authors.name AS author_name, publication_date";
    public static final String FROM_POLLS = " FROM polls LEFT JOIN authors ON polls.author = authors.id";
    private static final Map<POLL_LIST_SORTING_TYPE, String> sorters = Map.of
            (
                    POLL_LIST_SORTING_TYPE.DEFAULT,
                    " ",
                    POLL_LIST_SORTING_TYPE.MOST_LIKES,
                    " ORDER BY (SELECT COALESCE(SUM(liked), 0) FROM uservotes WHERE polls.id = uservotes.poll_id) DESC",
                    POLL_LIST_SORTING_TYPE.PUBLICATION_DATE,
                    " ORDER BY publication_date DESC"
            );
    //The map values are Suppliers of String instead of simply returning String. This is because when
    // dynamically building the filter we are assuming and casting the filterValue input to a different type.
    private Map<POLL_LIST_FILTER, Supplier<String>> filtersMap(Object filterValue) {
        return Map.of
                (
                        POLL_LIST_FILTER.NO_FILTER,
                        ()->" WHERE 1=1",
                        POLL_LIST_FILTER.AUTHOR,
                        ()->" WHERE author = '" + ((Author) filterValue).getIndex().toString() + "'",
                        POLL_LIST_FILTER.BROADCAST,
                        ()->" WHERE publication_date IS" + ((Boolean)filterValue?" NOT":"" ) + " NULL"
                );
    }


    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Random randomGenerator = new Random();


    @Override
    public int insertPoll(UUID id, Poll poll)
    {
        String sqlStatement = "INSERT INTO polls (id, question, author, publication_date) values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlStatement, pollToParams(id, poll));
        return 1;
    }

    @Override
    public Poll getPollById(UUID id) {
        String sqlStatement = SELECT_POLLS_COLUMNS + FROM_POLLS + " WHERE polls.id = ?";
        Poll poll = jdbcTemplate.queryForObject(sqlStatement, new PollRowMapper(), id.toString());

        return poll;
    }

    @Override
    public Poll getRandomPoll() {
        int totalPolls = getPollCount();
        if (totalPolls == 0) return null;
        int randIndex = randomGenerator.nextInt(totalPolls);

        String sqlStatement = SELECT_POLLS_COLUMNS + FROM_POLLS + " LIMIT " + randIndex + ", 1";
        Poll poll = jdbcTemplate.queryForObject(sqlStatement, new PollRowMapper());

        return poll;
    }

    //OOS for now
    @Override
    public int updatePoll(Poll poll)
    {
        return -1;
    }

    @Override
    public List<Poll> getPolls(POLL_LIST_SORTING_TYPE sortingType, POLL_LIST_FILTER filter, Object filterValue, PaginationInfo paginationInfo)
    {
        String sqlStatement = SELECT_POLLS_COLUMNS
                + FROM_POLLS
                + filtersMap(filterValue).get(filter).get()
                + sorters.get(sortingType)
                + " LIMIT " + (paginationInfo.getPageStart() - 1) + ", " + paginationInfo.getPageSize();
        List<Poll> polls = jdbcTemplate.query(sqlStatement, new PollRowMapper());

        String countStatement = "SELECT COUNT(*) "
                + FROM_POLLS
                + filtersMap(filterValue).get(filter).get();
        int filteredCount = jdbcTemplate.queryForObject(countStatement, Integer.TYPE);

        paginationInfo.setTotalSize(filteredCount);

        return polls;
    }

    @Override
    public int getPollCount()
    {
        String sqlStatement = "SELECT COUNT(*) FROM polls";
        int rowCount = jdbcTemplate.queryForObject(sqlStatement, Integer.TYPE);
        return rowCount;
    }

    private Object[] pollToParams(UUID id, Poll poll)
    {
        ArrayList paramList = new ArrayList();
        paramList.add(id.toString());
        paramList.add(poll.getQuestion());
        paramList.add(poll.getAuthor().getIndex().toString());
        paramList.add(poll.getPublicationDate());

        return  paramList.toArray();
    }

    private static class PollRowMapper implements RowMapper<Poll> {
        @Override
        public Poll mapRow(ResultSet resultSet, int i) throws SQLException {

            String authorName = resultSet.getString("author_name");
            Author author = Optional.ofNullable(resultSet.getString("author_id"))
                    .map(aid -> new Author(UUID.fromString(aid), authorName))
                    .orElse(null);

            Poll poll = new Poll(
                    resultSet.getString("question"),
                    author,
                    null,
                    resultSet.getDate("publication_date")
            );
            poll.setId(UUID.fromString(resultSet.getString("poll_id")));

            return poll;
        }
    }
}
