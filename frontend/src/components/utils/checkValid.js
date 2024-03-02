/**
 * 문자열이 유효한지 여부 확인.
 * @param str 검증이 필요한 내용
 * @returns {boolean} 유효한지 여부
 */
export function checkString(str){
    return (str??null) !== null && str.length > 0 && str !== "null";
}