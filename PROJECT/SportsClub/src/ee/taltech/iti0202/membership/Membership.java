package ee.taltech.iti0202.membership;

import ee.taltech.iti0202.sportsclub.SportsClub;

/**
 * Membership class.
 * Sports club sets up membership price. If someone buys membership, they become member
 * and added to sports club system.
 */

public abstract class Membership {

    protected final SportsClub sportsClub;

    /**
     * Constructor for membership.
     * @param sportsClub The SportsClub instance to fetch membership price from.
     */
    public Membership(SportsClub sportsClub) {
        this.sportsClub = sportsClub;
    }

    /**
     * Get sports club.
     * @return sport club.
     */
    public SportsClub getSportsClub() {
        return sportsClub;
    }

    /**
     * Get sports club id.
     * @return sport club.
     */
    public int getSportsClubId() {
        return sportsClub.getId();
    }

    /**
     * Get price of membership.
     * @return price.
     */
    public abstract double getPrice();

    /**
     * Get type of membership.
     * @return type.
     */
    public abstract String getType();


    /**
     * Builder class for Membership.
     */
    public abstract static class Builder {

        protected SportsClub sportsClub;

        /**
         * Method to set the sports club for the membership.
         * @param sportsClub The SportsClub instance.
         * @return The builder instance.
         */
        public Builder ofSportsClub(SportsClub sportsClub) {
            this.sportsClub = sportsClub;
            return this;
        }

        /**
         * Method to build the Membership instance.
         * @return The Membership instance.
         */
        public abstract Membership build();
    }
}
