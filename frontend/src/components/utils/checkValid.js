/**
 * 문자열이 유효한지 여부 확인.
 * @param {String} str
 * @returns {boolean} 유효한지 여부
 */
export function checkString(str) {
	return (str ?? null) !== null && str.length > 0 && str.toUpperCase() !== "NULL";
}
