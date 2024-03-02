import {dateThatCanChangeYear} from "./dateFunction.js";

/**
 * 현재 쿼리값이 없을 경우 기본 값을 넣어 반환
 * @param query route query 내용
 * @returns {Map<any, any>} 검색 조건 내용.
 */
export function queryParser(query){
    const end = dateThatCanChangeYear(null);
    const start = dateThatCanChangeYear(-1);
    const obj = query !== null? {...query} : {}
    return new Map()
        .set("page" , (obj["page"]??null) !== null ? obj["page"] : 1)
        .set("keyword" , (obj["keyword"]??null) !== null ? obj["keyword"] : null)
        .set("end" , (obj['end']??null) !== null ? obj["end"]: end)
        .set("start" , (obj['start']??null) !== null ? obj["start"]: start)
        .set("contentCategoryId" , (obj["contentCategoryId"]??null) !== null ? obj["contentCategoryId"] : null)
}