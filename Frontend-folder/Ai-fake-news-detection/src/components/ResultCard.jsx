const verdictColorMap = {
  real: {
    label: 'Real',
    ring: 'border-emerald-400/40',
    bg: 'bg-emerald-400/15',
    text: 'text-emerald-300',
    progress: 'from-emerald-400 to-emerald-300',
  },
  fake: {
    label: 'Fake',
    ring: 'border-rose-400/40',
    bg: 'bg-rose-400/15',
    text: 'text-rose-300',
    progress: 'from-rose-400 to-rose-300',
  },
  suspicious: {
    label: 'Suspicious',
    ring: 'border-amber-300/40',
    bg: 'bg-amber-300/15',
    text: 'text-amber-200',
    progress: 'from-amber-300 to-amber-200',
  },
}

function normalizeVerdict(verdict) {
  if (!verdict) {
    return 'suspicious'
  }

  const value = verdict.toLowerCase()
  if (value.includes('real') || value.includes('true')) {
    return 'real'
  }

  if (value.includes('fake') || value.includes('false')) {
    return 'fake'
  }

  return 'suspicious'
}

function ResultCard({ result }) {
  if (!result) {
    return null
  }

  const normalizedVerdict = normalizeVerdict(result.verdict)
  const palette = verdictColorMap[normalizedVerdict]
  const confidence = Math.max(0, Math.min(100, Number(result.confidence) || 0))

  return (
    <section className="mx-auto w-full max-w-6xl animate-fade-in-up px-4 pb-16 pt-10 sm:px-6 lg:px-8">
      <div className={`glass-panel rounded-3xl border p-6 sm:p-8 ${palette.ring}`}>
        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <h3 className="font-orbitron text-2xl font-bold text-white">Analysis Result</h3>
          <span
            className={`inline-flex w-fit items-center rounded-full border border-white/10 px-4 py-2 text-sm font-semibold uppercase tracking-wide ${palette.bg} ${palette.text}`}
          >
            Verdict: {palette.label}
          </span>
        </div>

        <div className="mt-6">
          <div className="mb-2 flex items-center justify-between text-sm text-slate-300">
            <span>Confidence Score</span>
            <span>{confidence}%</span>
          </div>
          <div className="h-3 overflow-hidden rounded-full bg-slate-800">
            <div
              className={`h-full rounded-full bg-gradient-to-r ${palette.progress} transition-all duration-700 ease-out`}
              style={{ width: `${confidence}%` }}
            />
          </div>
        </div>

        <div className="mt-6 rounded-2xl border border-white/10 bg-slate-900/50 p-4 sm:p-5">
          <p className="text-xs uppercase tracking-[0.2em] text-slate-400">AI Explanation</p>
          <p className="mt-2 text-sm leading-relaxed text-slate-200 sm:text-base">
            {result.explanation || 'No explanation provided by the AI service.'}
          </p>
        </div>
      </div>
    </section>
  )
}

export default ResultCard
