package ee.taltech.iti0202.strategy;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;

public interface DiscountStrategy {

    /**
     * Calculate the discount based on member activity.
     * @param member
     * @param sportsClub
     * @return discount.
     */
    double calculateDiscount(Member member, SportsClub sportsClub);
}
