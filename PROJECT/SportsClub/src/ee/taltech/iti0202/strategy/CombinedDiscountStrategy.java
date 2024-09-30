package ee.taltech.iti0202.strategy;

import ee.taltech.iti0202.member.Member;
import ee.taltech.iti0202.sportsclub.SportsClub;

public class CombinedDiscountStrategy implements DiscountStrategy {

    private static final double MAX_DISCOUNT_PERCENTAGE = 90.0;
    private final DiscountStrategy participationStrategy;
    private final DiscountStrategy bonusPointsStrategy;
    private final DiscountStrategy sportsTypeDiscountStrategy;

    /**
     * onstructs a CombinedDiscountStrategy with the specified strategies.
     * @param participationStrategy
     * @param bonusPointsStrategy
     */
    public CombinedDiscountStrategy(DiscountStrategy participationStrategy, DiscountStrategy bonusPointsStrategy,
                                    DiscountStrategy sportsTypeDiscountStrategy) {
        this.participationStrategy = participationStrategy;
        this.bonusPointsStrategy = bonusPointsStrategy;
        this.sportsTypeDiscountStrategy = sportsTypeDiscountStrategy;
    }

    @Override
    public double calculateDiscount(Member member, SportsClub sportsClub) {
        double discount1 = participationStrategy.calculateDiscount(member, sportsClub);
        double discount2 = bonusPointsStrategy.calculateDiscount(member, sportsClub);
        double discount3 = sportsTypeDiscountStrategy.calculateDiscount(member, sportsClub);

        double combinedDiscount = discount1 + discount2 + discount3;
        if (combinedDiscount > MAX_DISCOUNT_PERCENTAGE) {
            combinedDiscount = MAX_DISCOUNT_PERCENTAGE;
        }

        return combinedDiscount;
    }
}
