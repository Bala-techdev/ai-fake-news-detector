import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

export const analyzeNews = async (content) => {
  try {
    const response = await api.post('/analyze', { content })
    return response.data
  } catch (error) {
    if (error.response?.data?.message) {
      throw new Error(error.response.data.message)
    }

    if (error.response?.data?.error) {
      throw new Error(error.response.data.error)
    }

    if (error.code === 'ECONNABORTED') {
      throw new Error('The request timed out. Please try again.')
    }

    if (error.message === 'Network Error') {
      throw new Error('Unable to reach backend at http://localhost:8080. Please ensure the Spring Boot server is running.')
    }

    throw new Error('Unable to analyze news right now. Please try again in a moment.')
  }
}

export default api
