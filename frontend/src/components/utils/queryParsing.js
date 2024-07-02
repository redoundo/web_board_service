import { dateFunction } from "./dateFunction.js";
import qs from "qs";
import { checkString } from "./checkValid.js";

const basicUrl = "https://xnp2tr132k.execute-api.ap-northeast-2.amazonaws.com/api-stage/api";
// const basicUrl = import.meta.env.VUE_APP_SERVER_URL;
// const basicUrl = "http://localhost:8080/api";

/**
 * 현재 쿼리값이 없을 경우 기본 값을 넣어 반환
 * @param query route query 내용
 * @returns {Map<any, any>} 검색 조건 내용.
 */
export function queryParser(query) {
	const end = dateFunction(null, null);
	const start = dateFunction("m", -1);
	const parsedUrl = urlQueryParser();
	const obj =
		query !== null && Object.keys(query).length > 0
			? { ...query }
			: parsedUrl !== null && Object.keys(parsedUrl).length > 0
			? { ...parsedUrl }
			: {};
	console.log(obj);
	const map = new Map()
		.set("page", (obj["page"] ?? null) !== null ? obj["page"] : 1)
		.set("keyword", (obj["keyword"] ?? null) !== null ? obj["keyword"] : null)
		.set("end", (obj["end"] ?? null) !== null ? obj["end"] : end)
		.set("start", (obj["start"] ?? null) !== null ? obj["start"] : start)
		.set("categoryId", (obj["categoryId"] ?? null) !== null ? obj["categoryId"] : null)
		.set("maxCount", (obj["maxCount"] ?? null) !== null ? obj["maxCount"] : 10)
		.set(
			"orderByColumn",
			(obj["orderByColumn"] ?? null) !== null ? obj["orderByColumn"] : "submitDate",
		)
		.set("orderByDesc", (obj["orderByDesc"] ?? null) !== null ? obj["orderByDesc"] : 0);

	if ((obj["inquiryHistory"] ?? null) !== null) map.set("inquiryHistory", obj["inquiryHistory"]);
	if ((obj["contentId"] ?? null) !== null) map.set("contentId", obj["contentId"]);
	return map;
}

/**
 * 쿼리 스트링을 포함한 url 을 만들어낸다.
 * @param url url 문자열
 * @param map map 타입의 쿼리스트링 내용
 * @returns {string}
 */
export function mapQueryString(url, map) {
	if ((map ?? null) === null || map.size === 0) return basicUrl + url;
	let queries = [];
	for (let key of map.keys()) {
		if (checkString(map.get(key))) queries.push(`${key}=${map.get(key)}`);
	}
	if (queries.length > 0) return basicUrl + url + "?" + queries.join("&");
	else return basicUrl + url;
}

/**
 * 쿼리 스트링을 포함한 url 을 만들어낸다.
 * @param url url 문자열
 * @param map 쿼리스트링 내용
 * @returns {*|string}
 */
export function queryString(url, map) {
	if ((map ?? null) === null || map.size === 0) return basicUrl + url;
	return basicUrl + url + "?" + qs.stringify(map, { arrayFormat: "repeat", skipNulls: true });
}

/**
 * 그냥 뒤로가기를 누르면 router.push 가 되어 있지 않아 url 에 queryString 이 존재하더라도 사용하지 못하게 된다.
 * 그래서 현재 url 에서 정보를 얻어올 수 있는 방법 제공.
 * @returns {any|{}|null}
 */
export function urlQueryParser() {
	const url = document.location.href;
	if (url.includes("?") === false) return null; // ? 이 없으면 queryString 이 없는 걸로 보고 null 반환.

	const queries = url.split("?")[1];
	if (queries.length > 0) return qs.parse(queries, { charset: "utf-8" });
	else return null;
}

export function deleteContentId(query) {
	if ((query ?? null) !== null && Object.keys(query).length > 0) return query;
	if (Object.keys(query).includes("contentId") === false) return query;
	delete query.contentId;
	return query;
}
