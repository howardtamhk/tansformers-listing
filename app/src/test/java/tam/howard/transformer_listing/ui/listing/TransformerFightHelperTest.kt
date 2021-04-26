package tam.howard.transformer_listing.ui.listing

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import tam.howard.transformer_listing.model.transformers.Transformer
import tam.howard.transformer_listing.model.transformers.TransformerTeam
import tam.howard.transformer_listing.ui.listing.model.TransformerFightResult

class TransformerFightHelperTest {

    lateinit var helper: TransformerFightHelper

    @Before
    fun setUp() {
        helper = TransformerFightHelper()
    }

    //region fight 1vs 1
    @Test
    fun `passing Deception to Autobot returns IllegalStateException`() {
        val deception1: Transformer = mockk {
            every { team } returns TransformerTeam.Decepticon
        }
        val deception2: Transformer = mockk {
            every { team } returns TransformerTeam.Decepticon
        }


        try {
            helper.fight(deception1, deception2)
            Assert.fail()
        } catch (e: IllegalStateException) {

        }
    }

    @Test
    fun `passing Autobot to Deception returns IllegalStateException`() {
        val autobot1: Transformer = mockk {
            every { team } returns TransformerTeam.Autobot
        }
        val autobot2: Transformer = mockk {
            every { team } returns TransformerTeam.Autobot
        }


        try {
            helper.fight(autobot1, autobot2)
            Assert.fail()
        } catch (e: IllegalStateException) {

        }
    }

    @Test
    fun `passing Autobot to Deception and vice versa returns IllegalStateException`() {
        val autobot: Transformer = mockk {
            every { team } returns TransformerTeam.Autobot
        }
        val deception: Transformer = mockk {
            every { team } returns TransformerTeam.Decepticon
        }


        try {
            helper.fight(deception, autobot)
            Assert.fail()
        } catch (e: IllegalStateException) {

        }
    }

    @Test
    fun `Optimus Prime vs Predaking returns AutoWinCompetition`() {
        val autobot: Transformer = mockk {
            every { team } returns TransformerTeam.Autobot
            every { name } returns "Optimus Prime"
        }
        val deception: Transformer = mockk {
            every { team } returns TransformerTeam.Decepticon
            every { name } returns "Predaking"
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.AutoWinCompetition)
    }

    @Test
    fun `Optimus Prime vs any transformer returns Autobot wins`() {
        val autobot: Transformer = mockk {
            every { team } returns TransformerTeam.Autobot
            every { name } returns "Optimus Prime"
        }
        val deception: Transformer = mockk {
            every { team } returns TransformerTeam.Decepticon
            every { name } returns "test"
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Autobot)
    }

    @Test
    fun `any vs Predaking returns Deception wins`() {
        val autobot: Transformer = mockk {
            every { team } returns TransformerTeam.Autobot
            every { name } returns "test"
        }
        val deception: Transformer = mockk {
            every { team } returns TransformerTeam.Decepticon
            every { name } returns "Predaking"
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Deception)
    }

    @Test
    fun `Autobot with 4 courage and 3 strength less returns Deception Wins`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { courage } returns 1
            every { strength } returns 1
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { courage } returns 5
            every { strength } returns 4
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Deception)
    }

    @Test
    fun `Autobot with 4 courage and 2 strength less, same rating returns Tie`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { courage } returns 1
            every { strength } returns 1
            every { rating } returns 10
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { courage } returns 5
            every { strength } returns 3
            every { rating } returns 10
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Tie)
    }

    @Test
    fun `Deception with 4 courage and 3 strength less returns Deception Wins`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { courage } returns 5
            every { strength } returns 4
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { courage } returns 1
            every { strength } returns 1
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Autobot)
    }

    @Test
    fun `Deception with 3 courage and 3 strength less, same rating returns Tie`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { courage } returns 1
            every { strength } returns 1
            every { rating } returns 10
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { courage } returns 4
            every { strength } returns 4
            every { rating } returns 10
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Tie)
    }

    @Test
    fun `Autobot with 3 skill less returns Deception Wins`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { skill } returns 1
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { skill } returns 4
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Deception)
    }

    @Test
    fun `Deception with 3 skill less returns Autobot Wins`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { skill } returns 4
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { skill } returns 1
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Autobot)
    }

    @Test
    fun `Deception with 2 skill less, same rating returns Tie`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { skill } returns 3
            every { rating } returns 10
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { skill } returns 1
            every { rating } returns 10
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Tie)
    }

    @Test
    fun `Autobot and Deception same rating returns Tie`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rating } returns 10
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rating } returns 10
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Tie)
    }

    @Test
    fun `Autobot rating larger than Deception returns Autobot win`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rating } returns 30
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rating } returns 10
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Autobot)
    }

    @Test
    fun `Deception rating larger than Autobot returns Deception win`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rating } returns 30
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rating } returns 50
        }

        assertThat(
            helper.fight(
                autobot,
                deception
            )
        ).isEqualTo(TransformerFightResult.WinningTeam.Deception)
    }
    //endregion fight 1vs 1

    //region fight list
    @Test
    fun `empty list returns noOfFight is 0`() {
        val result = helper.fight(listOf())
        assertThat(result.noOfFight).isEqualTo(0)
        assertThat(result.winningTeam).isNull()
        assertThat(result.winningTeamLastTransformer).isNull()
        assertThat(result.survivorFromLosingTeam).isEmpty()
    }

    @Test
    fun `empty Deception list returns noOfFight is 0`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
        }
        val result = helper.fight(listOf(autobot))
        assertThat(result.noOfFight).isEqualTo(0)
        assertThat(result.winningTeam).isNull()
        assertThat(result.winningTeamLastTransformer).isNull()
        assertThat(result.survivorFromLosingTeam).isEmpty()
    }

    @Test
    fun `empty Autobot list returns noOfFight is 0`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
        }
        val result = helper.fight(listOf(autobot))
        assertThat(result.noOfFight).isEqualTo(0)
        assertThat(result.winningTeam).isNull()
        assertThat(result.winningTeamLastTransformer).isNull()
        assertThat(result.survivorFromLosingTeam).isEmpty()
    }

    @Test
    fun `Autobot list size more than Deception list size returns noOfFight is Deception list size`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
        }
        val autobot2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
        }
        val result = helper.fight(listOf(autobot, deception, autobot2))
        assertThat(result.noOfFight).isEqualTo(1)
    }

    @Test
    fun `Deception list size more than Autobot list size returns noOfFight is Autobot list size`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
        }
        val deception2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
        }
        val result = helper.fight(listOf(autobot, deception, deception2))
        assertThat(result.noOfFight).isEqualTo(1)
    }

    @Test
    fun `Test Autobot fight order by rank`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 1
        }
        val autobot2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 10
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
        }

        val helperSpy = spyk(helper)
        helperSpy.fight(listOf(autobot, deception, autobot2))
        verify { helperSpy.fight(autobot2, deception) }
        confirmVerified()
    }

    @Test
    fun `Test Autobot fight order by rating after rank`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 10
            every { rating } returns 10
        }
        val autobot2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 10
            every { rating } returns 30
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
        }

        val helperSpy = spyk(helper)
        helperSpy.fight(listOf(autobot, deception, autobot2))
        verify { helperSpy.fight(autobot2, deception) }
        confirmVerified()
    }

    @Test
    fun `Test Deception fight order by rank`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 1
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 2
        }
        val deception2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
        }

        val helperSpy = spyk(helper)
        helperSpy.fight(listOf(autobot, deception, deception2))
        verify { helperSpy.fight(autobot, deception2) }
        confirmVerified()
    }

    @Test
    fun `Test Deception fight order by rating after rank`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 1
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
            every { rating } returns 30
        }
        val deception2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
            every { rating } returns 31
        }

        val helperSpy = spyk(helper)
        helperSpy.fight(listOf(autobot, deception, deception2))
        verify { helperSpy.fight(autobot, deception2) }
        confirmVerified()
    }

    @Test
    fun `Test Autobot wins more round than Deception returns Autobot win`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rating } returns 50
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
            every { rating } returns 30
        }
        val deception2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 1
            every { rating } returns 50
        }

        val helperSpy = spyk(helper)
        every {
            helperSpy.fight(
                autobot,
                deception
            )
        } returns TransformerFightResult.WinningTeam.Autobot
        val result = helperSpy.fight(listOf(autobot, deception, deception2))
        assertThat(result.noOfFight).isEqualTo(1)
        assertThat(result.winningTeam).isEqualTo(TransformerFightResult.WinningTeam.Autobot)
        assertThat(result.winningTeamLastTransformer).isEqualTo(autobot)
        assertThat(result.survivorFromLosingTeam).containsExactly(deception2)
    }

    @Test
    fun `Test Deception wins more round than Autobot returns Autobot win`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 5
            every { rating } returns 20
        }
        val autobot2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 1
            every { rating } returns 50
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
            every { rating } returns 30
        }

        val helperSpy = spyk(helper)
        every {
            helperSpy.fight(
                autobot,
                deception
            )
        } returns TransformerFightResult.WinningTeam.Deception
        val result = helperSpy.fight(listOf(autobot, deception, autobot2))
        assertThat(result.noOfFight).isEqualTo(1)
        assertThat(result.winningTeam).isEqualTo(TransformerFightResult.WinningTeam.Deception)
        assertThat(result.winningTeamLastTransformer).isEqualTo(deception)
        assertThat(result.survivorFromLosingTeam).containsExactly(autobot2)
    }

    @Test
    fun `Test Autobot win tie Deception win returns Tie`() {
        val autobot: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rating } returns 40
        }
        val deception: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
            every { rating } returns 40
        }
        val deception2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 1
            every { rating } returns 50
        }

        val helperSpy = spyk(helper)
        every {
            helperSpy.fight(
                autobot,
                deception
            )
        } returns TransformerFightResult.WinningTeam.Tie
        val result = helperSpy.fight(listOf(autobot, deception, deception2))
        assertThat(result.noOfFight).isEqualTo(1)
        assertThat(result.winningTeam).isEqualTo(TransformerFightResult.WinningTeam.Tie)
        assertThat(result.winningTeamLastTransformer).isNull()
        assertThat(result.survivorFromLosingTeam).isEmpty()
    }

    @Test
    fun `Test Optimus Prime vs Predaking is 2nd round returns AutoWinCompetition`() {
        val autobot1: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 5
            every { rating } returns 40
        }
        val autobot2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 2
            every { name } returns "Optimus Prime"
            every { rating } returns 50
        }
        val autobot3: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Autobot
            every { rank } returns 1
            every { rating } returns 10
        }
        val deception1: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 5
            every { rating } returns 40
        }
        val deception2: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 2
            every { name } returns "Predaking"
            every { rating } returns 50
        }
        val deception3: Transformer = mockk(relaxed = true) {
            every { team } returns TransformerTeam.Decepticon
            every { rank } returns 1
            every { rating } returns 20
        }

        val helperSpy = spyk(helper) {
            every {
                fight(
                    autobot1,
                    deception1
                )
            } returns TransformerFightResult.WinningTeam.Tie

            every {
                fight(
                    autobot2,
                    deception2
                )
            } returns TransformerFightResult.WinningTeam.AutoWinCompetition
        }

        val result = helperSpy.fight(
            listOf(
                autobot1,
                autobot2,
                autobot3,
                deception1,
                deception2,
                deception3
            )
        )
        assertThat(result.noOfFight).isEqualTo(2)
        assertThat(result.winningTeam).isEqualTo(TransformerFightResult.WinningTeam.AutoWinCompetition)
        assertThat(result.winningTeamLastTransformer).isNull()
        assertThat(result.survivorFromLosingTeam).isEmpty()
    }
    //endregion fight list
}