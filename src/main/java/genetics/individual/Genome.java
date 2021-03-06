package genetics.individual;

import com.google.common.base.MoreObjects;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;

import genetics.api.alleles.IAllele;
import genetics.api.alleles.IAlleleRegistry;
import genetics.api.individual.IChromosome;
import genetics.api.individual.IChromosomeType;
import genetics.api.individual.IGenome;
import genetics.api.individual.IKaryotype;

import genetics.ApiInstance;
import genetics.utils.AlleleUtils;

public final class Genome implements IGenome {
	private final IChromosome[] chromosomes;
	private final IKaryotype karyotype;

	public Genome(IKaryotype karyotype, NBTTagCompound compound) {
		this.karyotype = karyotype;
		this.chromosomes = GeneticSaveHandler.INSTANCE.readTag(karyotype, compound);
	}

	public Genome(IKaryotype karyotype, IChromosome[] chromosomes) {
		this.karyotype = karyotype;
		checkChromosomes(chromosomes);
		this.chromosomes = chromosomes;
	}

	@SuppressWarnings("all")
	private void checkChromosomes(IChromosome[] chromosomes) {
		if (chromosomes.length != karyotype.getChromosomeTypes().length) {
			String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template.\n%s", karyotype.getUID(), chromosomesToString(chromosomes));
			throw new IllegalArgumentException(message);
		}

		IAlleleRegistry registry = ApiInstance.INSTANCE.getAlleleRegistry();
		IChromosomeType[] chromosomeTypes = karyotype.getChromosomeTypes();
		for (int i = 0; i < chromosomeTypes.length; i++) {
			IChromosomeType chromosomeType = chromosomeTypes[i];
			IChromosome chromosome = chromosomes[i];
			if (chromosome == null) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Missing chromosome '%s'.\n%s", karyotype.getUID(), chromosomeType.getName(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			IAllele primary = chromosome.getActiveAllele();
			if (primary == null) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Missing active allele for '%s'.\n%s", karyotype.getUID(), chromosomeType.getName(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			IAllele secondary = chromosome.getInactiveAllele();
			if (secondary == null) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Missing inactive allele for '%s'.\n%s", karyotype.getUID(), chromosomeType.getName(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			if (!registry.isValidAllele(primary, chromosomeType)) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Incorrect type for active allele '%s'.\n%s.", karyotype.getUID(), chromosomeType.getName(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}

			if (!registry.isValidAllele(secondary, chromosomeType)) {
				String message = String.format("Tried to create a genome for '%s' from an invalid chromosome template. " +
					"Incorrect type for inaktive allele '%s'.\n%s.", karyotype.getUID(), chromosomeType.getName(), chromosomesToString(chromosomes));
				throw new IllegalArgumentException(message);
			}
		}
	}

	private String chromosomesToString(IChromosome[] chromosomes) {
		StringBuilder stringBuilder = new StringBuilder();
		IChromosomeType[] chromosomeTypes = karyotype.getChromosomeTypes();
		for (int i = 0; i < chromosomes.length; i++) {
			IChromosomeType chromosomeType = chromosomeTypes[i];
			IChromosome chromosome = chromosomes[i];
			stringBuilder.append(chromosomeType.getName()).append(": ").append(chromosome).append("\n");
		}

		return stringBuilder.toString();
	}

	// / INFORMATION RETRIEVAL
	@Override
	public IChromosome[] getChromosomes() {
		return Arrays.copyOf(chromosomes, chromosomes.length);
	}

	@Override
	public IAllele getActiveAllele(IChromosomeType chromosomeType) {
		IChromosome chromosome = getChromosome(chromosomeType);
		return chromosome.getActiveAllele();
	}

	@Override
	public IAllele getInactiveAllele(IChromosomeType chromosomeType) {
		IChromosome chromosome = getChromosome(chromosomeType);
		return chromosome.getInactiveAllele();
	}

	@Override
	public <V> V getActiveValue(IChromosomeType chromosomeType, Class<? extends V> valueClass) {
		IAllele allele = getActiveAllele(chromosomeType);
		V value = AlleleUtils.getAlleleValue(allele, valueClass, null);
		if (value == null) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the active position of the chromosome type '%s' has no value.", allele, chromosomeType));
		}
		return value;
	}

	@Override
	public <V> V getInactiveValue(IChromosomeType chromosomeType, Class<? extends V> valueClass) {
		IAllele allele = getInactiveAllele(chromosomeType);
		V value = AlleleUtils.getAlleleValue(allele, valueClass, null);
		if (value == null) {
			throw new IllegalArgumentException(String.format("The allele '%s' at the inactive position of the chromosome type '%s' has no value.", allele, chromosomeType));
		}
		return value;
	}

	@Override
	public IChromosome getChromosome(IChromosomeType chromosomeType) {
		return chromosomes[chromosomeType.getIndex()];
	}

	@Override
	public IAllele[] getActiveAlleles() {
		IAllele[] alleles = new IAllele[chromosomes.length];
		for (IChromosome chromosome : chromosomes) {
			alleles[chromosome.getType().getIndex()] = chromosome.getActiveAllele();
		}
		return alleles;
	}

	@Override
	public IAllele[] getInactiveAlleles() {
		IAllele[] alleles = new IAllele[chromosomes.length];
		for (IChromosome chromosome : chromosomes) {
			alleles[chromosome.getType().getIndex()] = chromosome.getInactiveAllele();
		}
		return alleles;
	}

	@Override
	public IKaryotype getKaryotype() {
		return karyotype;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return GeneticSaveHandler.INSTANCE.writeTag(chromosomes, karyotype, compound);
	}

	@Override
	public boolean isPureBred(IChromosomeType chromosomeType) {
		IChromosome chromosome = getChromosome(chromosomeType);
		return chromosome.isPureBred();
	}

	@Override
	public boolean isGeneticEqual(IGenome other) {
		IChromosome[] otherChromosomes = other.getChromosomes();
		if (chromosomes.length != otherChromosomes.length) {
			return false;
		}

		for (int i = 0; i < chromosomes.length; i++) {
			IChromosome chromosome = chromosomes[i];
			IChromosome otherChromosome = otherChromosomes[i];
			if (chromosome == null && otherChromosome == null) {
				continue;
			}
			if (chromosome == null || otherChromosome == null) {
				return false;
			}

			if (!chromosome.isGeneticEqual(otherChromosome)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(this);
		int i = 0;
		for (IChromosome chromosome : chromosomes) {
			toStringHelper.add(String.valueOf(i++), chromosome);
		}
		return toStringHelper.toString();
	}
}
