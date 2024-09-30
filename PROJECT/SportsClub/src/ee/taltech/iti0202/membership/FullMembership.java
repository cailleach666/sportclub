package ee.taltech.iti0202.membership;

import ee.taltech.iti0202.sportsclub.SportsClub;

/**
 * FullMembership class representing the full membership package.
 */
public class FullMembership extends Membership {

    private static final double FULL_MEMBERSHIP_FEE = 60.0;

    /**
     * Constructor for FullMembership.
     * @param sportsClub The SportsClub instance to fetch membership price from.
     */
    public FullMembership(SportsClub sportsClub) {
        super(sportsClub);
    }

    @Override
    public double getPrice() {
        return FULL_MEMBERSHIP_FEE;
    }

    @Override
    public String getType() {
        return "full";
    }

    /**
     * Builder class for FullMembership.
     */
    public static class Builder extends Membership.Builder {

        /**
         * Constructor for the builder.
         * @param sportsClub The SportsClub instance.
         */
        public Builder ofSportsClub(SportsClub sportsClub) {
            this.sportsClub = sportsClub;
            return this;
        }

        /**
         * Method to build the FullMembership instance.
         * @return The FullMembership instance.
         */
        @Override
        public FullMembership build() {
            return new FullMembership(sportsClub);
        }
    }
}
