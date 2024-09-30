package ee.taltech.iti0202.strategy;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;

public class SportsTypeDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculateDiscount(Member member, SportsClub sportsClub) {
        int uniqueSportsCount = member.getParticipatedSportPrevMonth();
        double discountPercentage = 0.0;
        if (uniqueSportsCount == 0) {
            discountPercentage += 0.0;
        }
        if (uniqueSportsCount <= 3) {
            discountPercentage = 0.0;
        } else {
            discountPercentage += 10.0;
        }

        return discountPercentage;
    }
}
