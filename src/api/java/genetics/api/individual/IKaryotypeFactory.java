package genetics.api.individual;

import java.util.function.Function;

import genetics.api.alleles.IAlleleTemplate;

public interface IKaryotypeFactory {
	/**
	 * Creates a IKaryotypeBuilder.
	 *
	 * @param speciesType The template type of the karyotype
	 * @return A IKaryotypeBuilder that can be used to build {@link IKaryotype}.
	 */
	IKaryotypeBuilder createKaryotype(String uid, IChromosomeType speciesType, Function<IKaryotype, IAlleleTemplate> defaultTemplate);

	/**
	 * Creates a IKaryotype
	 *
	 * @param enumClass A enum that implements {@link IChromosomeType}.
	 */
	<T extends Enum<T> & IChromosomeType> IKaryotype createKaryotype(String uid, Class<? extends T> enumClass, Function<IKaryotype, IAlleleTemplate> defaultTemplate);
}
