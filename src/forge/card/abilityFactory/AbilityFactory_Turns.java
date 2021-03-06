package forge.card.abilityFactory;

import forge.AllZone;
import forge.ComputerUtil;
import forge.Player;
import forge.card.spellability.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>AbilityFactory_Turns class.</p>
 *
 * @author Forge
 * @version $Id: $
 */
public class AbilityFactory_Turns {

    // *************************************************************************
    // ************************* ADD TURN **************************************
    // *************************************************************************

    /**
     * <p>createAbilityAddTurn.</p>
     *
     * @param af a {@link forge.card.abilityFactory.AbilityFactory} object.
     * @return a {@link forge.card.spellability.SpellAbility} object.
     */
    public static SpellAbility createAbilityAddTurn(final AbilityFactory af) {

        final SpellAbility abAddTurn = new Ability_Activated(af.getHostCard(), af.getAbCost(), af.getAbTgt()) {
            private static final long serialVersionUID = -3526200766738015688L;

            @Override
            public String getStackDescription() {
                return addTurnStackDescription(af, this);
            }

            @Override
            public boolean canPlayAI() {
                return addTurnCanPlayAI(af, this);
            }

            @Override
            public void resolve() {
                addTurnResolve(af, this);
            }

            @Override
            public boolean doTrigger(boolean mandatory) {
                return addTurnTriggerAI(af, this, mandatory);
            }

        };
        return abAddTurn;
    }

    /**
     * <p>createSpellAddTurn.</p>
     *
     * @param af a {@link forge.card.abilityFactory.AbilityFactory} object.
     * @return a {@link forge.card.spellability.SpellAbility} object.
     */
    public static SpellAbility createSpellAddTurn(final AbilityFactory af) {
        final SpellAbility spAddTurn = new Spell(af.getHostCard(), af.getAbCost(), af.getAbTgt()) {
            private static final long serialVersionUID = -3921131887560356006L;

            @Override
            public String getStackDescription() {
                return addTurnStackDescription(af, this);
            }

            @Override
            public boolean canPlayAI() {
                return addTurnCanPlayAI(af, this);
            }

            @Override
            public void resolve() {
                addTurnResolve(af, this);
            }

        };
        return spAddTurn;
    }

    /**
     * <p>createDrawbackAddTurn.</p>
     *
     * @param af a {@link forge.card.abilityFactory.AbilityFactory} object.
     * @return a {@link forge.card.spellability.SpellAbility} object.
     */
    public static SpellAbility createDrawbackAddTurn(final AbilityFactory af) {
        final SpellAbility dbAddTurn = new Ability_Sub(af.getHostCard(), af.getAbTgt()) {
            private static final long serialVersionUID = -562517287448810951L;

            @Override
            public String getStackDescription() {
                return addTurnStackDescription(af, this);
            }

            @Override
            public void resolve() {
                addTurnResolve(af, this);
            }

            @Override
            public boolean chkAI_Drawback() {
                return true;
            }

            @Override
            public boolean doTrigger(boolean mandatory) {
                return addTurnTriggerAI(af, this, mandatory);
            }

        };
        return dbAddTurn;
    }

    /**
     * <p>addTurnStackDescription.</p>
     *
     * @param af a {@link forge.card.abilityFactory.AbilityFactory} object.
     * @param sa a {@link forge.card.spellability.SpellAbility} object.
     * @return a {@link java.lang.String} object.
     */
    private static String addTurnStackDescription(AbilityFactory af, SpellAbility sa) {
        HashMap<String, String> params = af.getMapParams();
        StringBuilder sb = new StringBuilder();
        int numTurns = AbilityFactory.calculateAmount(af.getHostCard(), params.get("NumTurns"), sa);

        if (!(sa instanceof Ability_Sub))
            sb.append(sa.getSourceCard()).append(" - ");
        else
            sb.append(" ");

        ArrayList<Player> tgtPlayers;

        Target tgt = af.getAbTgt();
        if (tgt != null)
            tgtPlayers = tgt.getTargetPlayers();
        else
            tgtPlayers = AbilityFactory.getDefinedPlayers(sa.getSourceCard(), params.get("Defined"), sa);

        for (Player player : tgtPlayers)
            sb.append(player).append(" ");

        sb.append("takes ");
        if (numTurns > 1) {
            sb.append(numTurns);
        } else {
            sb.append("an");
        }
        sb.append(" extra turn");
        if (numTurns > 1) sb.append("s");
        sb.append(" after this one.");

        Ability_Sub abSub = sa.getSubAbility();
        if (abSub != null) {
            sb.append(abSub.getStackDescription());
        }

        return sb.toString();
    }

    /**
     * <p>addTurnCanPlayAI.</p>
     *
     * @param af a {@link forge.card.abilityFactory.AbilityFactory} object.
     * @param sa a {@link forge.card.spellability.SpellAbility} object.
     * @return a boolean.
     */
    private static boolean addTurnCanPlayAI(final AbilityFactory af, final SpellAbility sa) {
        return addTurnTriggerAI(af, sa, false);
    }

    /**
     * <p>addTurnTriggerAI.</p>
     *
     * @param af a {@link forge.card.abilityFactory.AbilityFactory} object.
     * @param sa a {@link forge.card.spellability.SpellAbility} object.
     * @param mandatory a boolean.
     * @return a boolean.
     */
    private static boolean addTurnTriggerAI(final AbilityFactory af, final SpellAbility sa, boolean mandatory) {
        if (!ComputerUtil.canPayCost(sa))
            return false;

        HashMap<String, String> params = af.getMapParams();

        Target tgt = sa.getTarget();

        if (sa.getTarget() != null) {
            tgt.resetTargets();
            sa.getTarget().addTarget(AllZone.getComputerPlayer());
        } else {
            ArrayList<Player> tgtPlayers = AbilityFactory.getDefinedPlayers(sa.getSourceCard(), params.get("Defined"), sa);
            for (Player p : tgtPlayers)
                if (p.isHuman() && !mandatory)
                    return false;
            // not sure if the AI should be playing with cards that give the Human more turns.
        }
        return true;
    }

    /**
     * <p>addTurnResolve.</p>
     *
     * @param af a {@link forge.card.abilityFactory.AbilityFactory} object.
     * @param sa a {@link forge.card.spellability.SpellAbility} object.
     */
    private static void addTurnResolve(final AbilityFactory af, final SpellAbility sa) {
        HashMap<String, String> params = af.getMapParams();
        int numTurns = AbilityFactory.calculateAmount(af.getHostCard(), params.get("NumTurns"), sa);

        ArrayList<Player> tgtPlayers;

        Target tgt = af.getAbTgt();
        if (tgt != null)
            tgtPlayers = tgt.getTargetPlayers();
        else
            tgtPlayers = AbilityFactory.getDefinedPlayers(sa.getSourceCard(), params.get("Defined"), sa);

        for (Player p : tgtPlayers) {
            if (tgt == null || p.canTarget(af.getHostCard())) {
                for (int i = 0; i < numTurns; i++) {
                    AllZone.getPhase().addExtraTurn(p);
                }
            }
        }
    }

}//end class AbilityFactory_Turns
