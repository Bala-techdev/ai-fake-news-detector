import ResultCard from './ResultCard'

const EXAMPLE_FAKE_NEWS =
  'Breaking: Scientists confirm drinking silver nanoparticles daily reverses all chronic diseases in 72 hours. Hospitals are hiding this cure under pressure from pharmaceutical companies.'

function NewsAnalyzer({ newsText, onChange, onAnalyze, isLoading, onLoadExample, result }) {
  return (
    <section id="analyzer" className="mx-auto w-full max-w-6xl px-4 pt-14 sm:px-6 lg:px-8">
      <div className="glass-panel neon-border rounded-3xl p-6 sm:p-8">
        <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
          <h2 className="font-orbitron text-2xl font-bold text-white">News Analyzer</h2>
          <button
            type="button"
            onClick={() => onLoadExample(EXAMPLE_FAKE_NEWS)}
            className="rounded-lg border border-cyan-300/30 bg-cyan-300/10 px-4 py-2 text-xs font-semibold uppercase tracking-wide text-cyan-200 transition hover:border-cyan-300/50 hover:bg-cyan-300/20 sm:text-sm"
          >
            Try Example Fake News
          </button>
        </div>

        <p className="mt-3 text-sm text-slate-300 sm:text-base">
          Paste a news paragraph, post, or claim. The AI will classify it as Real, Fake, or Suspicious.
        </p>

        <textarea
          value={newsText}
          onChange={(event) => onChange(event.target.value)}
          placeholder="Paste news content here..."
          rows={8}
          className="mt-6 w-full resize-none rounded-2xl border border-white/15 bg-slate-950/80 p-4 text-sm text-slate-100 placeholder:text-slate-500 outline-none transition focus:border-cyan-300/50 focus:ring-2 focus:ring-cyan-300/25 sm:p-5 sm:text-base"
        />

        <div className="mt-6 flex flex-wrap items-center gap-4">
          <button
            type="button"
            onClick={onAnalyze}
            disabled={isLoading}
            aria-busy={isLoading}
            className={`inline-flex min-w-36 items-center justify-center rounded-xl bg-gradient-to-r from-fuchsia-500 via-blue-500 to-cyan-400 px-6 py-3 text-sm font-semibold text-white transition hover:scale-[1.02] hover:shadow-neon disabled:cursor-not-allowed disabled:opacity-70 ${
              isLoading ? 'animate-pulse shadow-[0_0_22px_rgba(56,189,248,0.55)]' : ''
            }`}
          >
            {isLoading ? (
              <span className="flex items-center gap-2">
                <span className="h-4 w-4 animate-spin rounded-full border-2 border-white/30 border-t-white" />
                Analyzing with AI...
              </span>
            ) : (
              'Analyze'
            )}
          </button>
        </div>

        <ResultCard result={result} />
      </div>
    </section>
  )
}

export default NewsAnalyzer
