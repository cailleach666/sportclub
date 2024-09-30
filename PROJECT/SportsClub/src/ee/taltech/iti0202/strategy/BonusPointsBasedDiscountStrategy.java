package ee.taltech.iti0202.strategy;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;

public class BonusPointsBasedDiscountStrategy implements DiscountStrategy {

    private static final double MAX_DISCOUNT_PERCENTAGE = 20.0;
    private static final double VALUE_OF_POINT = 0.25;

    @Override
    public double calculateDiscount(Member member, SportsClub sportsClub) {
        int bonusPointsPrev = member.getBonusPointsPrevMonth();
        double averagePointsPrev = sportsClub.getAverageBonusPointsPrevMonth();
        if (bonusPointsPrev == 0 || averagePointsPrev == 0) {
            return 0.0;
        }
        double discountPercentage = 0.0;

        if (bonusPointsPrev > averagePointsPrev) {
            int bonusPointsExcess = bonusPointsPrev - (int) averagePointsPrev;
            discountPercentage = Math.min(VALUE_OF_POINT * bonusPointsExcess, MAX_DISCOUNT_PERCENTAGE);
        }
        return discountPercentage;
    }
}
