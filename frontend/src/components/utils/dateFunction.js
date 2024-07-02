/**
 * 오늘 날짜를 구해주는 함수.
 * @param change number (ex.-1 , 2)
 * @returns {string} 오늘 날짜를 문자열로 반환.
 */
export function dateThatCanChangeYear(change) {
	let now = new Date();
	let year = now.getFullYear();
	let month = now.getMonth();
	let day = now.getDate();
	if (change != null) {
		year = year + Number(change);
	}
	if (month < 10) {
		month = "0" + month;
	}
	if (day < 10) {
		day = "0" + day;
	}

	return year + "-" + month + "-" + day;
}

/**
 * 현재 혹은 조정된 날짜 반환
 * @param position 바꿀 위치
 * @param change 바꿀 날짜의 값
 * @returns {string} 날짜
 */
export function dateFunction(position, change) {
	const now = new Date();
	let year = now.getFullYear();
	let month = now.getMonth() + 1;
	let day = now.getDate();
	if ((position ?? null) !== null && (change ?? null) !== null) {
		switch (position.toLowerCase()) {
			case "m":
				month = month + change;
				break;
			case "y":
				year = year + change;
				break;
			case "d":
				day = day + change;
				break;
		}
	}
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;

	return year + "-" + month + "-" + day;
}

/**
 * Array 타입으로 온 submitDate 를 문자열로 바꾼다.
 * @param submit submitDate Array 값
 * @returns {string} string 으로 만든 submitDate Array
 */
export function submitDateToStr(submit) {
	if (submit instanceof Array)
		return `${submit[0]}-${submit[1]}-${submit[2]} ${submit[3]}:${submit[4]}:${submit[5]}`;
	return numberSubmitDate(submit);
}

export function numberSubmitDate(number) {
	const date = (number ?? null) === null ? new Date() : new Date(number);
	let year = date.getFullYear();
	let month = date.getMonth() + 1;
	let day = date.getDate();
	if (month < 10) month = "0" + month;
	if (day < 10) day = "0" + day;
	let hour = date.getHours();
	let minute = date.getMinutes();
	let second = date.getSeconds();
	if (hour < 10) hour = "0" + hour;
	if (minute < 10) minute = "0" + minute;
	if (second < 10) second = "0" + second;
	return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
}
