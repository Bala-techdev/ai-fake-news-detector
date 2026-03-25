import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

export const analyzeNews = async (content) => {
  const response = await api.post('/analyze', { content })
  return response.data
}

export default api
