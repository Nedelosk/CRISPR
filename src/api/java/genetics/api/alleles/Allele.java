package genetics.api.alleles;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import net.minecraft.client.resources.I18n;

import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * A default implementation for a simple allele.
 */
public class Allele<V> extends IForgeRegistryEntry.Impl<IAllele<?>> implements IAllele<V> {
	private final V value;
	private final boolean dominant;
	private final String unlocalizedName;

	public Allele(String unlocalizedName, V value, boolean dominant) {
		this.unlocalizedName = unlocalizedName;
		this.value = value;
		this.dominant = dominant;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public boolean isDominant() {
		return dominant;
	}

	@Override
	public int hashCode() {
		return getRegistryName() != null ? getRegistryName().hashCode() : Objects.hash(value, dominant);
	}

	@Override
	public String getLocalizedName() {
		return I18n.format(unlocalizedName);
	}

	@Override
	public final String getUnlocalizedName() {
		return unlocalizedName;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IAllele)) {
			return false;
		}
		IAllele otherAllele = (IAllele) obj;
		return getRegistryName() != null ?
			getRegistryName().equals(((IAllele) obj).getRegistryName()) :
			Objects.equals(value, otherAllele.getValue()) && dominant == otherAllele.isDominant();
	}

	@Override
	public String toString() {
		return MoreObjects
			.toStringHelper(this)
			.add("name", getRegistryName())
			.add("value", value)
			.add("dominant", dominant)
			.add("unloc", unlocalizedName)
			.add("loc", getLocalizedName())
			.toString();
	}
}
