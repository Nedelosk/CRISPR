package genetics.classification;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Locale;

import net.minecraft.client.resources.I18n;

import genetics.api.alleles.IAlleleClassified;
import genetics.api.classification.IClassification;

import genetics.ApiInstance;

public class Classification implements IClassification {

	private final EnumClassLevel level;
	private final String uid;
	private final String scientific;
	@Nullable
	private IClassification parent;

	private final ArrayList<IAlleleClassified> members = new ArrayList<>();
	private final ArrayList<IClassification> groups = new ArrayList<>();

	public Classification(EnumClassLevel level, String uid, String scientific) {
		this.level = level;
		this.uid = level.name().toLowerCase(Locale.ENGLISH) + "." + uid;
		this.scientific = scientific;
		ApiInstance.INSTANCE.getClassificationRegistry().registerClassification(this);
	}

	@Override
	public EnumClassLevel getLevel() {
		return level;
	}

	@Override
	public String getUID() {
		return uid;
	}

	@Override
	@Nullable
	public IClassification getParent() {
		return parent;
	}

	@Override
	public void setParent(IClassification parent) {
		this.parent = parent;
	}

	@Override
	public String getScientific() {
		return this.scientific;
	}

	@Override
	public String getName() {
		return I18n.format("genetics." + uid);
	}

	@Override
	public String getDescription() {
		return I18n.format("genetics." + uid + ".description");
	}

	@Override
	public IClassification[] getMemberGroups() {
		return groups.toArray(new IClassification[groups.size()]);
	}

	@Override
	public void addMemberGroup(IClassification group) {
		groups.add(group);
		group.setParent(this);
	}

	@Override
	public IAlleleClassified[] getMemberSpecies() {
		return members.toArray(new IAlleleClassified[0]);
	}

	@Override
	public void addMemberSpecies(IAlleleClassified species) {
		members.add(species);
	}

}