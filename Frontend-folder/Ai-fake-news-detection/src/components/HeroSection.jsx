function HeroSection() {
  return (
    <section id="home" className="relative mx-auto w-full max-w-6xl px-4 pt-14 sm:px-6 lg:px-8 lg:pt-20">
      <div className="glass-panel neon-border relative overflow-hidden rounded-3xl px-6 py-14 sm:px-10">
        <div className="absolute -left-14 -top-14 h-44 w-44 rounded-full bg-fuchsia-500/20 blur-3xl" aria-hidden="true" />
        <div className="absolute -bottom-10 right-0 h-52 w-52 rounded-full bg-cyan-400/20 blur-3xl" aria-hidden="true" />

        <p className="font-orbitron text-xs uppercase tracking-[0.35em] text-cyan-300">
          Trust Signals, Not Noise
        </p>
        <h1 className="mt-5 max-w-3xl font-orbitron text-3xl font-extrabold leading-tight text-white sm:text-5xl">
          Detect Fake News Using AI
        </h1>
        <p className="mt-5 max-w-2xl text-sm leading-relaxed text-slate-300 sm:text-base">
          Paste any suspicious article, headline, or social media claim to get an instant AI-powered verdict with confidence scoring and transparent explanation.
        </p>

        <a
          href="#analyzer"
          className="mt-8 inline-flex items-center rounded-xl bg-gradient-to-r from-fuchsia-500 via-blue-500 to-cyan-400 px-6 py-3 text-sm font-semibold text-white transition hover:scale-[1.02] hover:shadow-neon"
        >
          Start Analysis
        </a>
      </div>
    </section>
  )
}


export default HeroSection
