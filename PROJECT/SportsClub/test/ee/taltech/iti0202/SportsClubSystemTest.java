package ee.taltech.iti0202;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.membership.FullMembership;
import ee.taltech.iti0202.membership.StandardMembership;
import ee.taltech.iti0202.sportsclub.SportsClub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SportsClubSystemTest {


    private SportsClubSystem sportsClubSystem;
    private SportsClub sportsClub1;
    private SportsClub sportsClub2;
    private SportsClub sportsClub3;

    /**
     * Creating setUp method.
     */
    @BeforeEach
    void setUp() {
        sportsClubSystem = SportsClubSystem.getInstance();
        sportsClubSystem.getSportsClubsInArea().clear();
    }

    @Test
    public void testGetInstanceSingletonMethodTest() {
        SportsClubSystem instance1 = SportsClubSystem.getInstance();
        SportsClubSystem instance2 = SportsClubSystem.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testSportsClubCreatedAndAddedToTheSystem() {
        SportsClub sportsClub1 = new SportsClub.Builder().build();
        assertTrue(sportsClubSystem.getSportsClubsInArea().contains(sportsClub1));
    }

    @Test
    public void testFewSportsClubsCreatedAndAddedToTheSystem() {
        sportsClub1 = new SportsClub.Builder().build();
        sportsClub2 = new SportsClub.Builder().build();
        assertEquals(2, sportsClubSystem.getSportsClubsInArea().size());
    }

    @Test
    public void testGetAllSportsClubsInfo() {
        sportsClub1 = new SportsClub.Builder().build();
        sportsClub2 = new SportsClub.Builder().build();
        StringBuilder expectedResult = new StringBuilder()
                .append("All sports clubs in this area:\n")
                        .append("Sports club ").append(sportsClub1.getId()).append("\n")
                                .append("Sports club ").append(sportsClub2.getId()).append("\n");
        assertEquals(expectedResult.toString(), sportsClubSystem.getAllSportsClubsInfo());
    }

    @Test
    void testMemberCanGetOverviewOfMemberships() {
        sportsClub1 = new SportsClub.Builder().build();
        sportsClub2 = new SportsClub.Builder().build();
        sportsClub3 = new SportsClub.Builder().build();
        Member member1 = new Member.Builder()
                .withName("Anton")
                .withBudget(155.0)
                .build();
        member1.buyMembership(new StandardMembership(sportsClub1));
        member1.buyMembership(new FullMembership(sportsClub2));
        StringBuilder expectedResult = new StringBuilder()
                .append("All sports clubs in this area:\n").append("Sports club ").append(sportsClub1.getId())
                        .append(" - standard membership obtained\n").append("Sports club ").append(sportsClub2.getId())
                        .append(" - full membership obtained\n").append("Sports club ").append(sportsClub3.getId())
                        .append(" - ").append("No membership obtained\n");
        assertEquals(expectedResult.toString(), member1.getSportsClubMembershipsInfo(sportsClubSystem));
    }

}
