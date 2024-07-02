import axios from "axios";
import qs from "qs";
import { jwtParser } from "../../utils/jwtparser.js";
// const url = "http://localhost:8080/api";
const url = "https://xnp2tr132k.execute-api.ap-northeast-2.amazonaws.com/api-stage/api";

const limits = { notify: 6, question: 5, board: 5, gallery: 3 };

axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.headers.common.Authorization = jwtParser.get();

export default {
	async get({ path, onSuccess, onError }) {
		axios.defaults.headers.common.Authorization = jwtParser.get();
		return await axios
			.get(path)
			.catch((err) => {
				if ((onError ?? null) === null) gotError(err);
				else onError(err);
			})
			.then((value) => {
				if ((onSuccess ?? null) === null) gotData(value);
				else onSuccess(value);
			});
	},
	async post({ path, data, onSuccess, onError, header = null }) {
		axios.defaults.headers.common.Authorization = jwtParser.get();
		if (header !== null && header === "multipart/form-data") {
			axios.defaults.headers.post["Content-Type"] = "multipart/form-data";
		} else {
			axios.defaults.headers.post["Content-Type"] = "application/json";
		}
		console.log(axios.defaults.headers.post["Content-Type"]);
		console.log(data);
		return await axios
			.post(path, data)
			.then((value) => {
				if ((onSuccess ?? null) === null) gotData(value);
				else onSuccess(value);
			})
			.catch((err) => {
				if ((onError ?? null) === null) gotError(err);
				else onError(err);
			});
	},
};

/**
 * 기본 에러 처리 함수
 * @param err 에러 내용
 */
function gotError(err) {
	console.log(err);
}

/**
 * 기본 데이터 처리 함수
 * @param value 데이터
 */
function gotData(value) {
	console.log(value?.data);
}
axios.defaults.headers.post["Content-Type"] = "application/json";
/**
 * jwt 토큰이 존재하는지 확인한 뒤, 존재한다면 사용자 닉네임을 찾아줄 axios 생성한 뒤 반환.
 * @returns {Promise<AxiosResponse<any>>|null}
 */
export function findUserByJwt() {
	const token = jwtParser.get();
	if (jwtParser.validity(token) === false) return null;
	const asyncAxios = async () =>
		await axios.post(url + "/user/main", {}, { headers: { Authorization: token } });
	return asyncAxios();
}

/**
 * 각 게시판 별 가져올 수 있는 개수만큼 반환하게끔 서버에 전달.
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export async function mainContentsWithLimit() {
	return await axios.post(url + "/main", {
		notify: 6,
		question: 5,
		board: 5,
		gallery: 3,
	});
}

/**
 * 아이디와 비번을 서버에 넘겨 로그인 진행.
 * @param id 사용자가 입력한 아이디
 * @param password 비밀번호
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export async function loginAxios(id, password) {
	return await axios.post(url + "/auth/login", { id: id, password: password });
}

export async function canUseThisId(id) {
	return await axios.post(url + "/auth/signIn/checkId", { id: id });
}

/**
 * 회원가입 진행.
 * @param id 사용자가 입력한 아이디
 * @param password 비밀번호
 * @param nickname 닉네임.
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export async function signInAxios(id, password, nickname) {
	return await axios.post(url + "/auth/signIn", {
		id: id,
		password: password,
		name: nickname,
	});
}
/**
 * 검색 조건으로 공지사항 게시글 가져오기
 * @param options 검색 조건
 * @param table 검색할 db table
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export async function contentAxios(options, table) {
	const queryString = qs.stringify(options, {
		arrayFormat: "repeat",
		skipNulls: true,
	});
	const axiosUrl =
		queryString.length > 0 ? url + "/board/" + table + queryString : url + "/board/" + table;
	return await axios.get(axiosUrl);
}

/**
 * 게시글 아이디로 게시글 내용을 찾아 반환.
 * @param id 찾고 싶은 게시글의 아이디
 * @param table 게시글의 아이디를 찾을 db table
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export async function findContentByIdAxios(id, table) {
	console.log(id);
	return await axios.get(url + "/" + table + "/view" + "?contentId=" + id);
}

/**
 * 게시글 삭제.
 * @param id 삭제 대상인 게시글의 아이디
 * @param table 게시글이 위치한 db table 이름
 * @returns {Promise<axios.AxiosResponse<any>|null>}
 */
export async function deleteContentWithJwt(id, table) {
	const parameter = ["board", "gallery"].includes(table) ? "/free/" + table : table;
	const queryString = url + "/view/" + parameter + "/delete?contentId=" + id;
	return await axios.get(queryString, { headers: { Authorization: jwtToken } });
}

/**
 * 입력한 내용을 db 에 저장.
 * @param table 저장할 db table 의 이름
 * @param value db 에 저장핳 내용
 * @returns {Promise<axios.AxiosResponse<any>|null>}
 */
export async function insertContentAxios(table, value) {
	const jwtToken = jwtParser.get();
	if ((jwtToken ?? null) === null) return null;
	const parameter = ["board", "gallery"].includes(table) ? "/free/" + table : table;
	return await axios.post(url + "/board" + parameter + "/write/insert", {
		content: value,
	});
}

export async function insertContentWithFiles(path, data) {
	const jwtToken = jwtParser.get();
	if ((jwtToken ?? null) === null) return null;
	axios.defaults.headers.post["Content-Type"] = "multipart/form-data";
	// axios.defaults.headers.common.Authorization = jwtParser.get();
	return await axios.post(path, data, { headers: { Authorization: jwtToken } });
}

/**
 * 상황에 맞는 카테고리 전체 가져오기
 * @param table db table 이름
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export async function getAllCategories(table) {
	const parameter = ["board", "gallery"].includes(table) ? "/free/" + table : table;
	return await axios.get(url + "/board/" + parameter + "/write");
}

/**
 * 파일 다운로드
 * @param fileName 다운로드할 파일의 이름
 * @param filePath 다운로드할 파일이 위치한 곳
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export async function downloadFile(fileName, filePath) {
	return await axios.post(url + "/download", {
		fileName: fileName,
		filePath: filePath,
	});
}

export async function updateContentWithFile(table, value, newFiles, uploadedFiles) {
	const jwtToken = jwtParser.get();
	return await axios.post(
		url + "/board/free/" + table + "/update",
		{ content: value, newFile: newFiles, uploadedFile: uploadedFiles },
		{ headers: { Authorization: jwtToken } },
	);
}

export async function updateContent(table, value) {
	const jwtToken = jwtParser.get();
	return await axios.post(
		url + "/board/free/" + table + "/update",
		{ content: value },
		{ headers: { Authorization: jwtToken } },
	);
}

export async function validateUser() {
	return await axios.get(url + "/user/validate");
}
