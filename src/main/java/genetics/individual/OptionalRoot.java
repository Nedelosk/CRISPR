package genetics.individual;

import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

import genetics.api.root.IIndividualRoot;
import genetics.api.root.IOptionalRoot;

public class OptionalRoot<R extends IIndividualRoot> implements IOptionalRoot<R> {
	@Nullable
	private R root = null;

	public void setRoot(@Nullable R definition) {
		this.root = definition;
	}

	@Override
	public Optional<R> maybeDefinition() {
		return Optional.ofNullable(root);
	}

	@Override
	public R get() {
		if (root == null) {
			throw new NoSuchElementException("No value present");
		}
		return root;
	}

	@Override
	public boolean isPresent() {
		return root != null;
	}

	@Override
	public void ifPresent(Consumer<R> consumer) {
		if (root != null) {
			consumer.accept(root);
		}
	}
}
