import axios from 'axios'

export default class MyUploadAdapter {
	constructor(loader) {
		// Save Loader instance to update upload progress.
		this.loader = loader
	}

	async upload() {
		const data = new FormData()
		data.append('type', 'image')
		data.append('file', await this.loader.file)

		return new Promise((resolve, reject) => {
			axios({
				url: `/resource/image`,
				method: 'post',
				data,
				headers: {
					'Authorization': 'Bearer tokenxxxxxxxxxxxxxxxxxxx'  // 此处为你定义的token 值(Bearer token 接口认证方式)
				}
				//withCredentials: true // 此处可删掉，没发现有什么用
			}).then(res => {
				var resData = res.data.data
				resData.default = resData.url
				resolve(resData)
			}).catch(error => {
				reject(error)
			})
		})
	}
}