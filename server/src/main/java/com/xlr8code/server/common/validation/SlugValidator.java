package com.xlr8code.server.common.validation;

import com.xlr8code.server.common.exception.DuplicateSlugInLanguagesException;
import com.xlr8code.server.common.exception.SlugAlreadyExistsException;

import java.util.Collection;
import java.util.HashSet;

public interface SlugValidator<T> {

    /**
     * @param slug slug to check
     * @return true if slug exists in the database
     */
    boolean existsBySlug(String slug);

    /**
     * @param slug        slug to check
     * @param ownerEntity owner to exclude
     * @return true if slug exists in the database and owner is not the owner
     */
    boolean existsBySlugAndOwnerNot(String slug, T ownerEntity);

    /**
     * @param slug slug to validate
     * @throws SlugAlreadyExistsException if slug already exists
     */
    default void validateSlug(String slug) {
        if (existsBySlug(slug)) {
            throw new SlugAlreadyExistsException(slug);
        }
    }

    /**
     * @param slug  slug to validate
     * @param owner owner to exclude
     * @throws SlugAlreadyExistsException if slug already exists
     */
    default void validateSlug(String slug, T owner) {
        if (existsBySlugAndOwnerNot(slug, owner)) {
            throw new SlugAlreadyExistsException(slug);
        }
    }

    /**
     * @param slugs slugs to validate
     * @throws DuplicateSlugInLanguagesException if there are duplicate slugs
     * @throws SlugAlreadyExistsException        if any slug already exists
     */
    default void validateSlugInList(Collection<String> slugs) {
        this.validateDuplicateSlugs(slugs);
        validateSlugInCollection(slugs);
    }

    /**
     * @param slugs slugs to validate
     * @param owner owner to exclude
     * @throws DuplicateSlugInLanguagesException if there are duplicate slugs
     */
    default void validateSlugInList(Collection<String> slugs, T owner) {
        this.validateDuplicateSlugs(slugs);
        validateSlugInCollection(slugs, owner);
    }

    /**
     * @param slugs slugs to validate
     * @throws SlugAlreadyExistsException if any slug already exists
     * @see #validateSlug(String)
     */
    private void validateSlugInCollection(Collection<String> slugs) {
        slugs.forEach(this::validateSlug);
    }

    /**
     * @param slugs slugs to validate
     * @param owner owner to exclude
     * @throws SlugAlreadyExistsException        if any slug already exists
     * @throws DuplicateSlugInLanguagesException if there are duplicate slugs
     * @see #validateSlugInCollection(Collection)
     */
    private void validateSlugInCollection(Collection<String> slugs, T owner) {
        slugs.forEach(slug -> validateSlug(slug, owner));
    }


    /**
     * @param slugs slugs to validate
     * @throws DuplicateSlugInLanguagesException if there are duplicate slugs
     * @see #validateSlugInCollection(Collection)
     */
    private void validateDuplicateSlugs(Collection<String> slugs) {
        var slugSet = new HashSet<String>();
        for (var slug : slugs) {
            if (!slugSet.add(slug)) {
                throw new DuplicateSlugInLanguagesException(slug);
            }
        }
    }


}
