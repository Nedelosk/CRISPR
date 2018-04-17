package genetics.alleles;

import com.google.common.collect.HashMultimap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import genetics.api.alleles.AlleleCategorized;
import genetics.api.alleles.IAllele;
import genetics.api.alleles.IAlleleHandler;
import genetics.api.alleles.IAlleleKey;
import genetics.api.alleles.IAlleleRegistry;

import genetics.Genetics;
import genetics.plugins.PluginManager;

public class AlleleRegistry implements IAlleleRegistry {

	private static final int ALLELE_ARRAY_SIZE = 2048;

	/* ALLELES */
	private final HashMultimap<IAllele, IAlleleKey> keysByAllele = HashMultimap.create(ALLELE_ARRAY_SIZE, 1);
	private final HashMap<IAlleleKey, IAllele> alleleByKey = new LinkedHashMap<>(ALLELE_ARRAY_SIZE);
	private final ForgeRegistry<IAllele> registry;
	/*
	 * Internal Set of all alleleHandlers, which trigger when an allele or branch is registered
	 */
	private final Set<IAlleleHandler> handlers = new HashSet<>();

	public AlleleRegistry() {
		@SuppressWarnings("unchecked")
		RegistryBuilder<IAllele> builder = new RegistryBuilder()
			.setMaxID(ALLELE_ARRAY_SIZE)
			.setName(new ResourceLocation(Genetics.MOD_ID, "alleles"))
			.setType(IAllele.class);
		//Cast the registry to the class type so we can get the ids of the alleles
		this.registry = (ForgeRegistry<IAllele>) builder.create();
	}

	@Override
	public <V> IAlleleRegistry registerAllele(String category, String valueName, V value, boolean dominant, IAlleleKey... keys) {
		return registerAllele(new AlleleCategorized<>(PluginManager.getCurrentModId(), category, valueName, value, dominant), keys);
	}

	@Override
	public IAlleleRegistry registerAllele(IAllele allele, IAlleleKey... keys) {
		if (!registry.containsKey(allele.getRegistryName())) {
			registry.register(allele);
			handlers.forEach(h -> h.onRegisterAllele(allele));
		}
		addValidAlleleKeys(allele, keys);
		return this;
	}

	@Override
	public IAlleleRegistry addValidAlleleKeys(ResourceLocation registryName, IAlleleKey... keys) {
		Optional<IAllele> alleleOptional = getAllele(registryName);
		alleleOptional.ifPresent(allele -> {
			addValidAlleleKeys(allele, keys);
		});
		return this;
	}

	private void addValidAlleleKeys(IAllele allele, IAlleleKey... keys) {
		for (IAlleleKey key : keys) {
			keysByAllele.put(allele, key);
			alleleByKey.put(key, allele);
		}
		handlers.forEach(h -> h.onAddKeys(allele, keys));
	}

	@Override
	public Optional<IAllele> getAllele(IAlleleKey key) {
		return Optional.ofNullable(alleleByKey.get(key));
	}

	@Override
	public Optional<IAllele> getAllele(ResourceLocation location) {
		return Optional.ofNullable(registry.getValue(location));
	}

	@Override
	public Collection<IAlleleKey> getKeys(IAllele allele) {
		return keysByAllele.get(allele);
	}

	@Override
	public void registerHandler(IAlleleHandler handler) {
		this.handlers.add(handler);
	}

	@Override
	public Set<IAlleleHandler> getHandlers() {
		return handlers;
	}

	public int getId(IAllele allele) {
		return registry.getID(allele);
	}

	public int getId(ResourceLocation alleleName) {
		return registry.getID(alleleName);
	}

	@Nullable
	public IAllele getAllele(int id) {
		return registry.getValue(id);
	}

}
