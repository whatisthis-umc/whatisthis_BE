package umc.demoday.whatisthis.domain.post.converter;


import umc.demoday.whatisthis.domain.post.enums.Category;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryConverter {

    private static final String TIP_SUFFIX = "_TIP";
    private static final String ITEM_SUFFIX = "_ITEM";

    public static List<String> getDynamicCategories(String category) {
        if (category == null || category.isBlank()) {
            return Collections.emptyList();
        }

        final String upperCaseCategory = category.toUpperCase();

        // 1. "_TIP"으로 끝나는 경우
        if (upperCaseCategory.endsWith(TIP_SUFFIX)) {
            // Category Enum의 모든 상수를 스트림으로 변환하여 필터링
            return Arrays.stream(Category.values())
                    .map(Enum::name) // Enum 상수를 문자열 이름으로 변환 (e.g., LIFE_TIP)
                    .filter(name -> name.endsWith(TIP_SUFFIX)) // "_TIP"으로 끝나는지 확인
                    .collect(Collectors.toList()); // 리스트로 수집
        }

        // 2. "_ITEM"으로 끝나는 경우
        if (upperCaseCategory.endsWith(ITEM_SUFFIX)) {
            return Arrays.stream(Category.values())
                    .map(Enum::name)
                    .filter(name -> name.endsWith(ITEM_SUFFIX))
                    .collect(Collectors.toList());
        }

        // 3. 어느 조건에도 해당하지 않으면, 원래 카테고리만 리스트에 담아 반환
        return Collections.singletonList(upperCaseCategory);
    }
}