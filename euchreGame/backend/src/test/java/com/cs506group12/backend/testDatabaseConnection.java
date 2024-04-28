package com.cs506group12.backend;

import com.cs506group12.backend.models.Card;
import com.cs506group12.backend.models.GameRecord;
import com.cs506group12.backend.models.GameState;
import com.cs506group12.backend.models.User;
import com.cs506group12.backend.models.Card.SUIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.when;

import java.sql.*;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.mockito.*;
import org.junit.jupiter.api.*;

/**
 * Unit tests for DatabaseConnection class
 * @author eknepper
 */
public class testDatabaseConnection {

    @Mock
    private DataSource ds;

    @Mock
    private Connection c;

    @Mock
    private PreparedStatement pstmt;

    @Mock
    private PreparedStatement pstmt2;

    @Mock
    private Statement stmt;

    @Mock
    private ResultSet rs;

    @Mock
    private ResultSet rs2;

    /**
     * Sets up basic sql connection mocks used in each test method 
     */
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(c.prepareStatement(any(String.class))).thenReturn(pstmt);
        when(c.createStatement()).thenReturn(stmt);
        when(c.isClosed()).thenReturn(false);
        when(ds.getConnection()).thenReturn(c);
    }

    /**
     * Tests storing a user in the SQL database
     */
    @Test
    public void testStoreUser() throws Exception {
        when(rs.isBeforeFirst()).thenReturn(false);
        when(pstmt.executeQuery()).thenReturn(rs);

        DatabaseConnection dbc = new DatabaseConnection(ds);

        assert(dbc.storeUser("1","1"));
    }

    /**
     * Tests if a duplicate user can be stored to the SQL database. The isBeforeFirst 
     * method is checked to see if there is a result in the database for a user with the
     * given name. When it returns true, there is already a user, so storeUser should
     * return false.
     */
    @Test
    public void testDuplicateUser() throws Exception {
        when(rs.isBeforeFirst()).thenReturn(true);
        when(pstmt.executeQuery()).thenReturn(rs);

        DatabaseConnection dbc = new DatabaseConnection(ds);

        assert(!dbc.storeUser("1", "1"));
    }

    /**
     * Tests storing a game record to the SQL database
     */
    @Test
    public void testStoreGameRecord() throws Exception{
        when(rs2.getInt(1)).thenReturn(1);
        when(rs2.getInt(2)).thenReturn(2);
        when(rs2.getInt(3)).thenReturn(3);
        when(rs2.getInt(4)).thenReturn(4);
        when(c.prepareStatement(any(String.class))).thenReturn(pstmt2).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(pstmt2.executeQuery()).thenReturn(rs2);

        DatabaseConnection dbc = new DatabaseConnection(ds);
        try{    
            dbc.storeGameRecord(new String[]{"a","b","c","d"}, new int[]{1,10}, new Timestamp(0), new Timestamp(1));
            assert(true);
        }catch(Exception e){
            assert(false);
        }

    }

    /**
     * Tests game record retrieval from SQL database
     */
    @Test
    public void testGetGameRecord() throws Exception{
        when(c.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        GameRecord gr1 = new GameRecord(1, new Timestamp(0), new Timestamp(1), new String[]{"a","b","c","d"}, new int[]{10,5});
        GameRecord gr2 = new GameRecord(2, new Timestamp(2), new Timestamp(3), new String[]{"e","f","g","h"}, new int[]{1,10});
        
        when(rs.getInt(1)).thenReturn(1).thenReturn(2);
        when(rs.getTimestamp(2)).thenReturn(new Timestamp(0)).thenReturn(new Timestamp(2));
        when(rs.getTimestamp(3)).thenReturn(new Timestamp(1)).thenReturn(new Timestamp(3));
        when(rs.getString(4)).thenReturn("a").thenReturn("e");
        when(rs.getString(5)).thenReturn("b").thenReturn("f");
        when(rs.getString(6)).thenReturn("c").thenReturn("g");
        when(rs.getString(7)).thenReturn("d").thenReturn("h");
        when(rs.getInt(8)).thenReturn(10).thenReturn(1);
        when(rs.getInt(9)).thenReturn(5).thenReturn(10);

        ArrayList<GameRecord> validationList = new ArrayList<GameRecord>();

        validationList.add(gr1);
        validationList.add(gr2);

        DatabaseConnection dbc = new DatabaseConnection(ds);

        ArrayList<GameRecord> testList = dbc.getGameRecords(1);

        GameRecord testRecord;
        GameRecord validationRecord;
        for(int i=0; i<2 ; i++){
            testRecord = testList.get(i);
            validationRecord = validationList.get(i);

            assert(testRecord.getGameUID() == validationRecord.getGameUID());
            assert(testRecord.getStartTime().equals(validationRecord.getStartTime()));
            assert(testRecord.getEndTime().equals(validationRecord.getEndTime()));
            assert(testRecord.getTeamNumber("a") == validationRecord.getTeamNumber("a"));
            assert(testRecord.getTeamNumber("e") == validationRecord.getTeamNumber("e"));
            assert(testRecord.getScore(1) == validationRecord.getScore(1));
            assert(testRecord.getScore(2) == validationRecord.getScore(2));
        }
    }

    /**
     * Tests user retrieval from SQL database
     */
    @Test
    public void testGetUser() throws Exception {
        when(pstmt.executeQuery()).thenReturn(rs);
        User validationUsr = new User(1,"1", null);

        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getString(2)).thenReturn(null);

        DatabaseConnection dbc = new DatabaseConnection(ds);

        User testUsr = dbc.getUser("1", "");

        assert(testUsr.getName().equals(validationUsr.getName()));
        assert(testUsr.getUserUID() == validationUsr.getUserUID());

    }

    @Test
    public void testLoadGameState() throws Exception{
        when(stmt.executeQuery(anyString())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString(intThat(i -> i >= 12 && i <= 15))).thenReturn("9H,14S,12D,11C");
        when(rs.getString(intThat(i -> i >=1 && i <=4))).thenReturn("testName");
        when(rs.getInt(16)).thenReturn(1);
        when(rs.getInt(17)).thenReturn(2);
        when(rs.getInt(8)).thenReturn(7);
        when(rs.getInt(9)).thenReturn(5);
        when(rs.getInt(10)).thenReturn(2);
        when(rs.getInt(11)).thenReturn(0);
        when(rs.getInt(19)).thenReturn(2);
        when(rs.getInt(20)).thenReturn(-1);
        when(rs.getString(18)).thenReturn("C");

        DatabaseConnection dbc = new DatabaseConnection(ds);
        GameState state = dbc.loadGameState("1", null);

        assertEquals(1, state.getDealerPosition());
        assertEquals(2, state.getLeadingPlayerPosition());
        assertEquals(7, state.getTeamScore(1));
        assertEquals(5, state.getTeamScore(2));
        assertEquals(2, state.getTeamTricks(1));
        assertEquals(0, state.getTeamTricks(2));
        assertEquals(Card.SUIT.CLUBS, state.getTrump());
        assertEquals(2, state.getAttackingTeam());
        assertEquals(-1, state.getPlayerGoingAlone());
        assertEquals(new Card(SUIT.SPADES,14),state.getHand(1).getCard(1));
    }
}
