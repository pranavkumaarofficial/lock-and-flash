Project context

This project implements the UI for a premium nighttime wellness app called REST.

The visual design is final and locked.
Design decisions have already been made using Stitch and must be treated as source of truth.

You are not designing.
You are faithfully translating an existing design into production-quality code.

Canonical reference

The following are canonical and must be matched exactly:

The provided Stitch-generated HTML/CSS

The visual screenshots derived from Stitch

The typography, spacing, motion, and restraint expressed in that code

If there is any conflict:
The Stitch output wins.

Do not “clean up”, “simplify”, “enhance”, or “modernize” unless explicitly instructed.

Aesthetic intent (do not reinterpret)

The REST UI is:

Editorial

Minimal

Calm

Nighttime

Premium

System-level (Pixel / Material-adjacent, not decorative)

The UI deliberately:

avoids explanation

avoids labels

avoids helper text

avoids affordance-heavy patterns

Silence, whitespace, and restraint are intentional.

ABSOLUTE RULES (non-negotiable)
1. No design invention

You must NOT:

add new UI text

add labels or helper copy

add icons not present in the design

add borders, dividers, or decorative elements

add shadows beyond those already specified

add gradients beyond those already specified

add motion that was not specified

If something feels “missing”, ask first.

2. Typography is locked

Typography is the primary brand signal. It must be respected exactly.

Fonts

Display / brand / hero: Newsreader, italic

UI / secondary text: system sans or explicitly specified sans only if present

You may NOT:

substitute fonts

add new font families

simulate italics via transforms

change letter spacing unless specified

The italic “r” and the wordmark “rest” are editorial typography, not UI labels.

3. Italic usage is sacred

Italics are used:

sparingly

intentionally

as a brand signal

You may NOT:

apply italics to buttons, labels, or utility text

add additional italic copy elsewhere

remove italics from the hero or logo

4. Color tokens are authoritative

Use the exact color values present in the Stitch code:

#6751a4 (primary)

#f7f6f7 (background light)

#17161c (background dark)

#FDFCFC (icon background)

existing opacity variants (e.g. /5, /10, /20)

Do not:

“adjust contrast”

“improve accessibility colors”

introduce new shades unless explicitly asked

5. Spacing and proportions must match

Spacing is part of the design language.

Respect paddings, margins, and gaps exactly as seen

Do not compress or expand layouts “for responsiveness” without instruction

Preserve negative space — empty space is intentional

Motion & animation rules

Animations are minimal, slow, and restrained.

Allowed:

opacity fades

subtle translateY (≤10px)

subtle scale (≤1.04)

Not allowed:

bounces

springs

elastic easing

attention-seeking motion

chained animations

If unsure, do less, not more.

Component philosophy
Primary control (play / pause)

Icon only

No text

Circular

Calm, centered

Reads as a system control, not a button

Do not convert it into:

a CTA

a card

a labeled control

a floating action button with text

Stats

Stats are informational, not interactive.

Flat

Quiet

No card emphasis

No visual competition with the hero

Logs

Logs are:

utilitarian

secondary

visually quieter than the hero

Do not decorate them.

Development mindset (important)

You are acting as:

a senior frontend engineer implementing a designer-approved UI

You are NOT acting as:

a designer, product manager, or UX improver

Correct mindset:

“Does this match the design?”

“Is this faithful?”

“Is this restrained enough?”

Incorrect mindset:

“Can I make this clearer?”

“Can I help the user more?”

“Can I add a label?”

When to ask questions

You MUST ask before proceeding if:

a layout decision is ambiguous

a responsive behavior is unclear

a font weight or size is missing

an interaction is not shown in the reference

Do NOT guess.

Success criteria

This task is successful if:

the coded UI visually matches the Stitch output

the typography feels identical

the UI remains calm, quiet, and premium

no additional explanation or UI noise has been introduced

If the result feels “friendlier” or “more helpful” than the design, it is wrong.

Final note

REST is intentionally understated.

The hardest part of this project is knowing when to stop.

When in doubt:

remove

simplify

ask

Faithfulness > cleverness.