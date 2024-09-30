package ee.taltech.iti0202.strategy;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;

public class ParticipationBasedDiscountStrategy implements DiscountStrategy {

    private static final double MAX_DISCOUNT_PERCENTAGE = 60.0;

    @Override
    public double calculateDiscount(Member member, SportsClub sportsClub) {
        int registeredSesPrevMonth = member.getRegisteredSesPrevMonth();
        double averageNumPreviousMonth = sportsClub.getAverageNumPreviousMonth();
        if (averageNumPreviousMonth == 0 || registeredSesPrevMonth == 0) {
            return 0.0;
        }
        double discountPercentage = 0.0;

        if (registeredSesPrevMonth > averageNumPreviousMonth) {
            discountPercentage = averageNumPreviousMonth / registeredSesPrevMonth * 100;
            discountPercentage = Math.min(discountPercentage, MAX_DISCOUNT_PERCENTAGE);
        }

        return discountPercentage;
    }
}
