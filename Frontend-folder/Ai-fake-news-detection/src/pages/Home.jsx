import { useState } from 'react'
import HeroSection from '../components/HeroSection'
import Navbar from '../components/Navbar'
import NewsAnalyzer from '../components/NewsAnalyzer'
import ResultCard from '../components/ResultCard'
import { analyzeNews } from '../services/api'

function Home() {
  const [newsText, setNewsText] = useState('')
  const [result, setResult] = useState(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')

  const handleAnalyze = async () => {
    const trimmedText = newsText.trim()

    if (!trimmedText) {
      setError('Please enter some news content before analysis.')
      return
    }

    setError('')
    setIsLoading(true)

    try {
      const response = await analyzeNews(trimmedText)
      setResult(response)
    } catch {
      setError('Unable to analyze news right now. Please check that backend is running on localhost:8080.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleLoadExample = (exampleText) => {
    setNewsText(exampleText)
    setError('')
  }

  return (
    <div className="relative overflow-hidden">
      <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_20%_20%,rgba(34,211,238,0.08),transparent_25%),radial-gradient(circle_at_80%_0%,rgba(168,85,247,0.18),transparent_35%),radial-gradient(circle_at_50%_100%,rgba(59,130,246,0.12),transparent_28%)]" />

      <Navbar />

      <main className="relative z-10">
        <HeroSection />

        <NewsAnalyzer
          newsText={newsText}
          onChange={setNewsText}
          onAnalyze={handleAnalyze}
          isLoading={isLoading}
          onLoadExample={handleLoadExample}
        />

        {error ? (
          <section className="mx-auto w-full max-w-6xl px-4 pt-6 sm:px-6 lg:px-8">
            <div className="rounded-2xl border border-rose-300/30 bg-rose-500/10 p-4 text-sm text-rose-200">
              {error}
            </div>
          </section>
        ) : null}

        <ResultCard result={result} />

        <section id="about" className="mx-auto w-full max-w-6xl px-4 pb-20 sm:px-6 lg:px-8">
          <div className="glass-panel rounded-3xl p-6 sm:p-8">
            <h3 className="font-orbitron text-2xl font-bold text-white">About</h3>
            <p className="mt-3 max-w-3xl text-sm leading-relaxed text-slate-300 sm:text-base">
              AI Fake News Detector combines language intelligence with credibility cues to help users quickly validate claims online. It is designed for fast fact-check support, transparent risk scoring, and better decision-making.
            </p>
          </div>
        </section>
      </main>
    </div>
  )
}

export default Home
