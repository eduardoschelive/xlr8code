package com.xlr8code.server.common.helper;

import com.xlr8code.server.common.enums.Language;
import com.xlr8code.server.common.utils.DoubleUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.*;

@Component
public class ApplicationLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    private static final Locale DEFAULT_LOCALE = Locale.of("en_US");
    private static final Language[] SUPPORTED_LANGUAGES = Language.values();
    private static final String LOCALE_QUALITY_SEPARATOR = ";";
    private static final String QUALITY_PARAMETER = "q=";
    private static final String LOCALE_SEPARATOR = ",";
    private static final Double DEFAULT_QUALITY_SCORE = 1.0;

    /**
     * @param parts the parts of the language tag
     * @return the locale string of the given language tag
     * @see Language#VARIANT_MAP
     * <p>
     * If the given language tag is not present in the variant map, the given language tag will be returned.
     * Otherwise, the locale string corresponding to the given language tag will be returned.
     * It is needed because of variants like "pt-BR" and "pt".
     */
    private static String getLocaleString(String parts) {
        // pt_BR -> pt-BR
        var replacedParts = parts.replace("-", "_");
        var splitLanguageCountry = replacedParts.split("_");

        if (splitLanguageCountry.length == 1) {
            return Language.VARIANT_MAP.getOrDefault(splitLanguageCountry[0], parts);
        }

        var transformedCode = splitLanguageCountry[0].toUpperCase() + "_" + splitLanguageCountry[1].toUpperCase();
        return Language.VARIANT_MAP.getOrDefault(transformedCode, transformedCode);
    }

    /**
     * <p>
     * Resolves the locale of the given request.
     * If the locale is not supported, the default locale will be returned.
     * The supported locales are defined in {@link ApplicationLocaleResolver#SUPPORTED_LANGUAGES}.
     * The default locale is defined in {@link ApplicationLocaleResolver#DEFAULT_LOCALE}.
     * The locale is retrieved from the request header "Accept-Language".
     * <p>
     *
     * @param request the request from which the locale will be resolved
     * @return the locale corresponding to the given request
     * @see Language
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        var acceptLanguageHeader = request.getHeader(ACCEPT_LANGUAGE_HEADER);
        var acceptedLanguages = parseAcceptLanguageHeader(acceptLanguageHeader);

        return this.getFirstSupportedLocale(acceptedLanguages);
    }

    public Set<Locale> getAllAcceptedLocales(HttpServletRequest request) {
        var allAcceptedLanguages = new HashSet<Locale>();
        var acceptLanguageHeader = request.getHeader(ACCEPT_LANGUAGE_HEADER);
        var acceptedLanguages = parseAcceptLanguageHeader(acceptLanguageHeader);

        for (var localeWithQualityScore : acceptedLanguages) {
            var locale = localeWithQualityScore.locale;
            if (isSupported(locale.getLanguage())) {
                allAcceptedLanguages.add(locale);
            }
        }

        if (allAcceptedLanguages.isEmpty()) {
            allAcceptedLanguages.add(DEFAULT_LOCALE);
        }

        return allAcceptedLanguages;
    }

    /**
     * @param acceptedLanguages the accepted languages with their quality scores sorted by their quality scores
     * @return the first supported locale from the given accepted languages
     */
    private Locale getFirstSupportedLocale(List<LocaleWithQualityScore> acceptedLanguages) {
        for (var localeWithQualityScore : acceptedLanguages) {
            var locale = localeWithQualityScore.locale;
            if (isSupported(locale.getLanguage())) {
                return locale;
            }
        }
        return DEFAULT_LOCALE;
    }

    /**
     * @param acceptLanguageHeader the accept language header to be parsed
     * @return the accepted languages with their quality scores sorted by their quality scores
     */
    private List<LocaleWithQualityScore> parseAcceptLanguageHeader(String acceptLanguageHeader) {
        var acceptedLanguages = this.getAcceptedLanguagesWithQualityScores(acceptLanguageHeader);
        sortAcceptedLanguagesByQualityScore(acceptedLanguages);
        return acceptedLanguages;
    }

    private void sortAcceptedLanguagesByQualityScore(List<LocaleWithQualityScore> acceptedLanguages) {
        if (acceptedLanguages.isEmpty()) {
            return;
        }
        acceptedLanguages.sort(Comparator.comparingDouble(LocaleWithQualityScore::qualityScore).reversed());
    }

    /**
     * @param acceptLanguageHeader the accept language header to be parsed
     * @return the accepted languages with their quality scores
     */
    private List<LocaleWithQualityScore> getAcceptedLanguagesWithQualityScores(String acceptLanguageHeader) {
        var acceptedLanguages = new ArrayList<LocaleWithQualityScore>();
        var localePosition = 0;

        if (ObjectUtils.isEmpty(acceptLanguageHeader)) {
            return List.of();
        }

        for (var language : acceptLanguageHeader.split(LOCALE_SEPARATOR)) {
            var languageTag = this.sanitizeLanguage(language);
            var parts = languageTag.split(LOCALE_QUALITY_SEPARATOR);
            var localeString = getLocaleString(parts[localePosition]);
            var qualityScore = this.getQualityScore(parts);

            var locale = Locale.of(localeString);
            acceptedLanguages.add(new LocaleWithQualityScore(locale, qualityScore));
        }
        return acceptedLanguages;
    }

    /**
     * @param parts the parts of the language tag
     * @return the quality score of the given language tag or the default quality score if the quality score is not present
     */
    private double getQualityScore(String[] parts) {
        for (String part : parts) {
            var trimmedPart = part.trim();
            if (trimmedPart.startsWith(QUALITY_PARAMETER)) {
                return parseQualityScore(trimmedPart);
            }
        }

        return DEFAULT_QUALITY_SCORE;
    }

    /**
     * @param qualityScore the quality score to be parsed
     * @return the parsed quality score or the default quality score if the given quality score is invalid
     */
    private double parseQualityScore(String qualityScore) {
        var qualityScoreValue = qualityScore.substring(QUALITY_PARAMETER.length());
        return DoubleUtils.tryParse(qualityScoreValue, DEFAULT_QUALITY_SCORE);
    }

    /**
     * @param language the language to be sanitized
     * @return the sanitized language
     */
    private String sanitizeLanguage(String language) {
        return language.trim();
    }

    /**
     * @param locale the locale to be checked
     * @return true if the given locale is supported, false otherwise
     */
    private boolean isSupported(String locale) {
        var supportedLocales = List.of(SUPPORTED_LANGUAGES);
        return supportedLocales.stream().anyMatch(supportedLocale -> supportedLocale.getCode().equalsIgnoreCase(locale));
    }

    private record LocaleWithQualityScore(Locale locale, double qualityScore) {
    }

}
