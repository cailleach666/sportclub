package ee.taltech.iti0202.membership;

import ee.taltech.iti0202.sportsclub.SportsClub;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MembershipTest {

    private SportsClub sportsClub;

    /**
     * Creating setUp method.
     */
    @BeforeEach
    void setUp() {
        sportsClub = new SportsClub.Builder()
                .build();

    }

    @org.junit.jupiter.api.Test
    public void testGetStandardMembershipSportsClub() {
        StandardMembership standardMembership = new StandardMembership(sportsClub);
        assertEquals(sportsClub, standardMembership.getSportsClub());
    }

    @org.junit.jupiter.api.Test
    public void testGetStandardMembershipSportsClubBuilder() {
        StandardMembership standardMembership = new StandardMembership.Builder()
                .ofSportsClub(sportsClub)
                .build();
        assertEquals(sportsClub, standardMembership.getSportsClub());
    }

    @org.junit.jupiter.api.Test
    public void testGetFullMembershipSportsClub() {
        FullMembership fullMembership = new FullMembership(sportsClub);
        assertEquals(sportsClub, fullMembership.getSportsClub());
    }

    @org.junit.jupiter.api.Test
    public void testGetStandardMembershipType() {
        StandardMembership standardMembership = new StandardMembership(sportsClub);
        assertEquals("standard", standardMembership.getType());
    }

    @org.junit.jupiter.api.Test
    public void testGetFullMembershipType() {
        FullMembership fullMembership = new FullMembership(sportsClub);
        assertEquals("full", fullMembership.getType());
    }

    @org.junit.jupiter.api.Test
    public void testGetFullMembershipSportsClubBuilder() {
        FullMembership fullMembership = new FullMembership.Builder()
                .ofSportsClub(sportsClub)
                .build();
        assertEquals(sportsClub, fullMembership.getSportsClub());
    }

    /**
     * Test if sports club contains two memberships.
     */
    @org.junit.jupiter.api.Test
    void testSportsClubContainsTwoMemberships() {
        assertEquals(2, sportsClub.getMemberships().size());
    }

    /**
     * Test if sports club contains FullMembership.
     */
    @org.junit.jupiter.api.Test
    void testSportsClubContainsFullMembership() {
        assertTrue(sportsClub.getMemberships().stream().anyMatch(membership -> membership instanceof FullMembership));
    }

    /**
     * Test if sports club contains StandardMembership.
     */
    @org.junit.jupiter.api.Test
    void testSportsClubContainsStandardMembership() {
        assertTrue(sportsClub.getMemberships().stream().anyMatch(membership ->
                membership instanceof StandardMembership));
    }

    /**
     * Test if the price of FullMembership is correct.
     */
    @org.junit.jupiter.api.Test
    void testFullMembershipPrice() {
        FullMembership fullMembership = new FullMembership(sportsClub);
        assertEquals(60.0, fullMembership.getPrice());
    }

    /**
     * Test if the price of StandardMembership is correct.
     */
    @org.junit.jupiter.api.Test
    void testStandardMembershipPrice() {
        StandardMembership standardMembership = new StandardMembership(sportsClub);
        assertEquals(40.0, standardMembership.getPrice());
    }
}
