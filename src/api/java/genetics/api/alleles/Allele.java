package genetics.api.alleles;

import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * A default implementation for a simple allele.
 */
public class Allele<V> extends IForgeRegistryEntry.Impl<IAllele<?>> implements IAllele<V> {
	private final V value;
	private final boolean dominant;

	public Allele(V value, boolean dominant) {
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
		return (value.hashCode() * 31) + Boolean.hashCode(dominant);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IAllele)) {
			return false;
		}
		IAllele otherAllele = (IAllele) obj;
		return value.equals(otherAllele.getValue()) && dominant == otherAllele.isDominant();
	}

	@Override
	public String toString() {
		return getRegistryName().toString();
	}
}
