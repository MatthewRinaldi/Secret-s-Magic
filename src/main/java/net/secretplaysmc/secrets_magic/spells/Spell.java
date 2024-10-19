package net.secretplaysmc.secrets_magic.spells;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifierRegistry;
import net.secretplaysmc.secrets_magic.spells.triggers.SpellTrigger;
import net.secretplaysmc.secrets_magic.spells.triggers.SpellTriggerRegistry;

import java.util.ArrayList;
import java.util.List;

public class Spell {
    private final String name;
    private final int manaCost;
    private final int castTime;
    private final SpellEffect effect;
    private final List<SpellModifier> modifiers;
    private final SpellTrigger trigger;

    public Spell(String name, int manaCost, int castTime, SpellEffect effect, List<SpellModifier> modifiers, SpellTrigger trigger) {
        this.name = name;
        this.manaCost = manaCost;
        this.castTime = castTime;
        this.effect = effect;
        this.modifiers = modifiers;
        this.trigger = trigger;
    }

    public void toNBT(CompoundTag tag) {
        tag.putString("name", this.name);
        tag.putInt("manaCost", this.manaCost);
        tag.putInt("castTime", this.castTime);

        tag.put("effect", this.effect.toNBT());

        ListTag modifierList = new ListTag();
        for (SpellModifier modifier : this.modifiers) {
            modifierList.add(modifier.toNBT());
        }
        tag.put("modifiers", modifierList);

        tag.put("trigger", this.trigger.toNBT());
    }

    public static Spell fromNBT(CompoundTag tag) {
        String name = tag.getString("name");
        int manaCost = tag.getInt("manaCost");
        int castTime = tag.getInt("castTime");

        SpellEffect effect = SpellEffect.fromNBT(tag.getCompound("effect"));

        List<SpellModifier> modifiers = new ArrayList<>();
        ListTag modifierList = tag.getList("modifiers", Tag.TAG_COMPOUND);
        for (Tag t : modifierList) {
            modifiers.add(SpellModifier.fromNBT((CompoundTag) t));
        }

        SpellTrigger trigger = SpellTrigger.fromNBT(tag.getCompound("trigger"));

        return new Spell(name, manaCost, castTime, effect, modifiers, trigger);
    }

    public int getManaCost() {
        return manaCost;
    }

    public void cast(Level world, ServerPlayer player, ItemStack wandItem) {
        trigger.execute(world, player, this);
    }

    public SpellEffect getEffect() {
        return effect;
    }

    public List<SpellModifier> getModifiers() {
        return modifiers;
    }

    public String getSpellName() {
        return name;
    }
}
