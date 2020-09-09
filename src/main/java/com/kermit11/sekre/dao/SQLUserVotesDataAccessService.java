package com.kermit11.sekre.dao;

import com.kermit11.sekre.model.Poll;
import com.kermit11.sekre.model.UserVotes;
import com.kermit11.sekre.model.VoteTotals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository("sqlUserVotesRepo")
public class SQLUserVotesDataAccessService implements UserVotesDao
{
    private static final String SELECT_UV_COLUMNS = "SELECT user_id, poll_id, liked, voted_for, voted_against";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int updateUserVotes(UUID pollID, String userID, UserVotes votes)
    {
        String sqlStatement = "REPLACE INTO uservotes (user_id, poll_id, liked, voted_for, voted_against) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlStatement, userID, pollID.toString(), votes.isLiked(), votes.isVotedFor(), votes.isVotedAgainst() );

        return 1;
    }

    @Override
    public UserVotes getUserVotes(UUID pollID, String userID)
    {
        String sqlStatement = "SELECT user_id, poll_id, liked, voted_for, voted_against"
                + " FROM uservotes"
                + " WHERE user_id = ? AND poll_id = ?";

        UserVotes userVotes = DataAccessUtils.singleResult(jdbcTemplate.query(sqlStatement, new UserVotesRowMapper(), userID, pollID.toString()));

        return Optional.ofNullable(userVotes).orElseGet(()->new UserVotes(userID, pollID, false, false, false));
    }

    @Override
    public VoteTotals getTotalVotes(UUID pollID)
    {
        String sqlStatement = "SELECT COALESCE(SUM(liked), 0) AS sum_liked, COALESCE(SUM(voted_for), 0) AS sum_for, COALESCE(SUM(voted_against), 0) AS sum_against"
                + " FROM uservotes"
                + " WHERE poll_id = ?";

        Map<String, Object> results = jdbcTemplate.queryForMap(sqlStatement, pollID.toString());

        int liked = ((BigDecimal)(results.get("sum_liked"))).intValue();
        int votedFor = ((BigDecimal)(results.get("sum_for"))).intValue();
        int votedAgainst = ((BigDecimal)(results.get("sum_against"))).intValue();

        VoteTotals totals = new VoteTotals(votedFor, votedAgainst, liked);

        return totals;
    }

    private static class UserVotesRowMapper implements RowMapper<UserVotes>
    {
        @Override
        public UserVotes mapRow(ResultSet resultSet, int i) throws SQLException
        {
            String userID = resultSet.getString("user_id");
            UUID pollID = UUID.fromString(resultSet.getString("poll_id"));
            boolean liked = resultSet.getBoolean("liked");
            boolean votedFor = resultSet.getBoolean("voted_for");
            boolean votedAgainst = resultSet.getBoolean("voted_against");
            UserVotes retVal = new UserVotes(userID, pollID, liked, votedFor, votedAgainst);

            return retVal;
        }
    }
}
