export function checkString(str){
    return (str??null) !== null && str.length > 0 && str !== "null";
}