package ee.taltech.iti0202.membership;

import ee.taltech.iti0202.sportsclub.SportsClub;

/**
 * StandardMembership class representing the standard membership package.
 */
public class StandardMembership extends Membership {

    private static final double STANDARD_MEMBERSHIP_FEE = 40.0;

    /**
     * Constructor for StandardMembership.
     * @param sportsClub The SportsClub instance to fetch membership price from.
     */
    public StandardMembership(SportsClub sportsClub) {
        super(sportsClub);
    }

    @Override
    public double getPrice() {
        return STANDARD_MEMBERSHIP_FEE;
    }

    @Override
    public String getType() {
        return "standard";
    }

    /**
     * Builder class for StandardMembership.
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
         * Method to build the StandardMembership instance.
         * @return The StandardMembership instance.
         */
        @Override
        public StandardMembership build() {
            return new StandardMembership(sportsClub);
        }
    }
}
