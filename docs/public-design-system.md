# Projectory Public UI Design System

## 1. Top-Level Design Guide

The public experience should feel like a clear, capable workspace: confident enough for serious project work, warm enough to feel inviting, and calm enough that the interface does not compete with the content.

The existing landing-page refresh achieves that through a small number of consistent ideas:

- **Clarity before decoration.** Use a strong headline, short supporting copy, and a clear next action. Prefer a few deliberate visual cues over many competing effects.
- **Warm, human accents over generic corporate blue.** The current design uses a muted neutral base and warm gold accents, with occasional blue-green supporting tones. These are examples of the mood, not mandatory color values.
- **Layered depth.** Separate content from its background with subtle borders, soft shadows, translucent surfaces, restrained blur, and overlapping elements. Keep depth purposeful and avoid making every element look elevated.
- **Editorial hierarchy.** Use prominent, tightly composed headlines; readable body text; small uppercase section labels; and generous spacing between ideas.
- **A product-led story.** Show what the product helps people do, then reinforce it with a product preview, feature summaries, useful content, and a clear call to action.
- **A coherent family of surfaces.** The home page, public subpages, authentication panel, navigation, and footer should look related, even where individual pages have different content or structure.

The existing implementation is available in [landingPage.html](../src/main/resources/templates/home/landingPage.html), [mainLayouts.html](../src/main/resources/templates/mainLayouts.html), and [landing-page.css](../src/main/resources/static/css/landing-page.css). Treat those files as examples of the design, not as restrictions on a future redesign.

## 2. Replication Guide

This guide is intended to let another designer or AI recreate the experience without copying the exact colors, dimensions, layout, imagery, or CSS.

### Preserve the intent

1. Make the product's value understandable at a glance.
2. Keep the primary action obvious and make secondary actions visually quieter.
3. Use a consistent visual language across navigation, panels, cards, buttons, and page endings.
4. Make both themes feel intentionally designed, not like one theme is an inversion of the other.
5. Adapt the composition to the amount and type of content instead of forcing every page into the same layout.

### Make independent design choices

The next implementation may choose a different:

- palette, accent hue, typography, icon family, illustration style, or photography;
- hero composition, card arrangement, section count, or decorative motif;
- border treatment, radius, shadow, animation, or spacing scale.

Those changes are welcome when they preserve the goals above. The existing values and composition are reference points only.

### Design with semantic tokens

Use a small, named set of design tokens rather than scattering unrelated values. At minimum, provide semantic roles for:

- page background and raised surface;
- primary and muted text;
- border/divider;
- primary accent and softer accent surface;
- primary and secondary actions;
- focus indicator;
- shadow/elevation.

Choose the actual values for the product and content. Maintain readable contrast for text, controls, icons, and focus states in both themes. Recheck overlays, muted text, borders, hover states, imagery, and footer links after changing the theme.

### Responsive and interaction principles

- Reflow grids and two-column hero layouts naturally on narrow screens; do not simply shrink desktop compositions.
- Keep navigation controls reachable and non-overlapping at every width.
- Preserve visible keyboard focus, usable touch targets, semantic headings, and meaningful alternative text.
- Motion should support feedback or hierarchy, remain brief, and respect reduced-motion preferences.
- Keep links and buttons behaviorally honest: links navigate, buttons perform actions.
- Retain existing application routes, authentication behavior, theme persistence, and content interactions when changing presentation.

## 3. Theme and Visual Foundations

### Light and dark themes

The current public pages use a light palette as the base and override semantic values for dark mode. The shared theme switch persists the selection and applies a `light-mode` or `dark-mode` class to the root element.

The intended result is not “white page” versus “black page.” In both themes:

- page surfaces should feel distinct from raised cards and dialogs;
- primary text should remain clear and secondary text should remain readable;
- accents should still identify emphasis and actions without becoming harsh;
- borders and shadows should remain visible but not noisy;
- images and translucent surfaces should retain enough contrast and context.

The navbar currently remains a dark translucent surface in both themes. This is a deliberate contrast anchor, not a rule: another design may instead theme the navbar if its controls and branding remain clear.

### Shape, depth, and texture

The refresh pairs rounded, card-like content surfaces with a few stronger shapes, such as the asymmetrical call-to-action panel and circular decorative outlines. Subtle dot patterns add texture to the closing panel. These are accents, not a requirement to use dots or circles everywhere.

Use a restrained shape vocabulary and repeat it across related elements. Shadows, borders, gradients, blur, and patterns should separate or guide—not add decoration without purpose.

### Patterned backgrounds and decorative ornaments

The closing call-to-action panel and the 404 illustration show two ways to add visual character without making the content depend on a large decorative image:

- The closing panel uses a low-contrast repeating dot texture over a theme-aware gradient, plus large outlined orbital shapes behind its illustration.
- The 404 illustration builds a small route/map scene from layered geometric shapes: a dotted field, circular guides, a dashed path, route markers, a destination tile, and floating workspace-card motifs.

The current 404 ornaments are composed with CSS rather than an SVG asset. SVG is another good option when it makes a custom, coherent illustration easier to create or maintain. The design intent is the same either way: create atmosphere and reinforce the page story while keeping content and controls clear.

#### A flexible construction process

1. **Start with a purpose.** Decide whether the ornament should suggest movement, planning, structure, collaboration, or another product idea. Do not add a pattern simply to fill empty space.
2. **Choose a small visual vocabulary.** Select a few compatible primitives—such as dots, arcs, lines, nodes, cards, or grids—and repeat them with variation. A limited vocabulary makes a complex illustration feel intentional.
3. **Establish the content layer first.** Lay out the heading, body copy, illustration subject, and actions. Put decorative layers behind or around that content; never make the text rely on the ornament for meaning.
4. **Build depth in deliberate layers.** A useful order is surface or gradient, low-contrast texture, larger structural forms, focal ornament, then the real content. Use opacity, scale, overlap, and blur sparingly to separate layers.
5. **Tune contrast to the theme.** Derive pattern and ornament colors from semantic theme tokens or theme-specific variants. Keep textures quieter than the content, but visible enough to register in both light and dark mode. Check the result on the actual background rather than judging colors in isolation.
6. **Control density and scale.** Patterns should not create moiré, visual noise, or unintended focal points. Let large ornaments crop naturally at container edges, and adjust size, position, or density for narrow screens.
7. **Protect interaction and accessibility.** Decorative elements should not intercept pointer events or keyboard focus. Mark non-informative SVGs and CSS art as hidden from assistive technology; if an illustration communicates unique information, provide an equivalent text description.
8. **Respect performance and motion.** Prefer CSS for simple repeatable primitives and SVG for detailed, scalable vector artwork. Avoid unnecessary large raster assets. If an ornament moves, keep the motion subtle and support reduced-motion preferences.

#### Choosing CSS, SVG, or an image

- **CSS** works well for simple geometry, repeating textures, gradients, borders, and small arrangements of shapes. It keeps simple ornaments themeable and easy to reposition.
- **SVG** works well for a composed illustration with a defined path, custom icons, or repeated vector details that need precise alignment and scale independence. Keep its shapes and colors understandable, and provide theme variants or use inherited/current colors where appropriate.
- **Raster imagery** works well for photography or artwork with rich tonal detail. It is less suitable for simple geometric decoration that needs to adapt to multiple themes.

These are practical options rather than rules. Select the technique that best serves the illustration, accessibility, responsive behavior, maintainability, and page performance.

### Typography and spacing

Use a clear scale with a small number of roles: display/headline, section heading, card heading, body, supporting text, and small label. The current design favors bold headings with tight line-height and letter spacing, contrasted with relaxed body copy.

Spacing should group related content and distinguish sections. Use consistent spacing relationships, but allow denser content such as forms and cards to use tighter internal rhythm than the hero or major section transitions.

## 4. Component and Section Guide

### 4.1 Public navigation bar

**Role:** Keep the brand, public destinations, theme control, and sign-in entry point discoverable without taking attention away from the page.

**Current treatment:** A compact, translucent dark bar with a blur effect sits over the opening content. Branding and links are light; navigation links gain a warm accent on hover or selection. Login and theme controls are pill-like, translucent controls aligned together.

**Replication guidance:**

- Treat the navbar as a shared component across public routes so controls do not drift visually or behaviorally.
- Keep the logo/name distinct from the destination links.
- Make active, hover, focus, and expanded navigation states apparent.
- Give theme and login controls adequate space and align them as a deliberate group.
- On mobile, ensure the collapsed menu and fixed-position controls do not cover each other.
- Keep the header contrast reliable over the content beneath it; a subtle scrim or solid surface can provide that contrast.

### 4.2 Theme toggle

**Role:** Make the light/dark choice immediately understandable and easy to operate.

**Current treatment:** A compact pill switch includes sun and moon indicators. It shares the navbar's translucent treatment and is backed by the application's persisted root theme class.

**Replication guidance:**

- Make it clear that this control changes appearance; do not rely on an unlabeled switch alone.
- Preserve keyboard operation, an accessible name, and a visible focus treatment.
- Ensure the indicator, thumb, and track are distinguishable in either theme.
- Keep the control synchronized with the theme already selected by the application.

### 4.3 Login and registration modal

**Role:** Offer account access without taking the user away from the public page.

**Current treatment:** A viewport-covering, blurred/dimmed scrim frames a centered rounded panel. The panel uses a raised surface, clear heading, close control, labeled fields, and a visually prominent submit action. It changes surface and text colors in dark mode while retaining bright form fields for legibility.

**Replication guidance:**

- Keep sign-in and sign-up visually related and make switching between them easy.
- Group the heading, fields, primary submit action, and alternative account action in that order.
- Make labels persistent and distinct from placeholder text.
- Keep error or status feedback visible and layered above the modal content.
- On small screens, let the panel use the available width and scroll when its content exceeds the viewport.
- Preserve the existing form endpoints, CSRF field, IDs/handlers used by the application, and open/close behavior unless deliberately changing the application contract.
- Provide keyboard focus visibility and ensure the close control has a clear accessible name.

### 4.4 Buttons and links

**Role:** Show action priority and make interactive elements feel consistent.

**Current treatment:** Primary calls to action use a warm, filled capsule shape with strong text contrast. Secondary hero actions use a transparent outlined surface. Text links use the accent and sometimes a directional icon. The Login control is a compact glassy pill.

**Replication guidance:**

- Define primary, secondary, quiet/text, and navigation-control roles.
- Use one clear primary action per visual region where possible.
- Keep labels concise and action-oriented; use icons only when they clarify or reinforce the action.
- Provide hover, active, focus, and disabled states that do not depend on color alone.
- Preserve consistent hit area and visual weight across the same action role.

### 4.5 Hero section

**Role:** Quickly explain the product and provide an obvious route into it or further exploration.

**Current treatment:** A large photographic background is softened with blur and dark overlays. A two-column composition places an eyebrow, headline, supporting statement, actions, and brief reassurance beside a floating product/workspace preview. The headline uses an accent highlight to create emphasis.

**Replication guidance:**

- Begin with a concise promise or outcome rather than a feature inventory.
- Use supporting copy to explain who the product helps and how.
- Pair a primary conversion action with a lower-commitment way to explore.
- If using a background image, protect text contrast with an overlay or another deliberate treatment.
- Use the product itself, a product preview, or a contextual illustration as the visual counterpart to the copy.
- Preserve enough whitespace around the main content that the hero remains readable and composed.
- On mobile, stack the content and visual in a logical order; do not let the preview make the hero excessively tall.

### 4.6 Bridge/value strip

**Role:** Carry the user from the hero into detailed content while summarizing a few core outcomes.

**Current treatment:** A compact, raised strip overlaps the bottom edge of the hero. Three short value statements pair a small icon with a bold phrase and a quieter explanation.

**Replication guidance:**

- Use this pattern only when there are a few genuinely useful summary points.
- Keep the items parallel in length and importance.
- Make the transition into the next page surface feel intentional, whether through overlap, spacing, a divider, or a change in background.
- Collapse or stack the items cleanly on small screens.

### 4.7 Feature and other in-between sections

**Role:** Explain capabilities in a scannable format and maintain momentum through the page.

**Current treatment:** A constrained content width, small section kicker, large heading, short explanatory text, and an evenly spaced grid of raised feature cards. Cards use a compact icon tile, title, and concise description. Soft borders and shadows keep the cards related to the page without overpowering it.

**Replication guidance:**

- Give each section a clear heading and a distinct purpose.
- Use cards when content is parallel; use prose or another structure when it is not.
- Keep card content scannable and avoid forcing long text into equal-height layouts.
- Use icons consistently, with meaning where possible; avoid decorative icon clutter.
- Vary the composition between sections when helpful, while retaining shared typography, spacing, and surface rules.
- Use motion sparingly. Content should not require a slow reveal animation before it can be read.

### 4.8 Editorial, blog, or insight section

**Role:** Add useful context and a softer path for visitors who are not ready to convert.

**Current treatment:** A section heading and a “visit” link introduce a small grid of image-led cards. Images share a consistent crop; copy has a predictable hierarchy; the article action is visually distinct.

**Replication guidance:**

- Treat content cards as previews, not miniature full articles.
- Keep image ratios consistent within a group and use useful alternative text.
- Make the title, summary, and next step easy to distinguish.
- Do not show the section when there is no content to populate it.
- Preserve any existing expand/collapse or navigation behavior when restyling.

### 4.9 Closing call-to-action (section before the footer)

**Role:** Give the page a purposeful ending and repeat a clear next step after the visitor has seen the supporting information.

**Current treatment:** A broad, contained panel uses an asymmetrical corner, a subtle dotted texture, and a theme-aware gradient. Copy and action sit beside an illustration with orbital outlines. It is visually stronger than a regular card but remains connected to the rest of the system through the same palette, typography, and surface language.

**Ornament notes:** The dots provide texture at a scale that does not compete with the copy. The large outlines frame and balance the artwork while extending beyond the panel to suggest scale and movement. These are layered background details, not content or controls. The pattern/ornament construction process above is a reusable reference, not a required recipe.

**Replication guidance:**

- Make the closing message complementary to—not a verbatim repetition of—the hero.
- Keep the final action clear and consistent with the site's primary action.
- Use one distinctive shape, illustration, or texture to mark the transition into the footer.
- Ensure text and controls remain legible in both themes.
- Avoid decorative assets that compete with the message or create overflow on mobile.

### 4.10 Footer

**Role:** Provide a quiet conclusion and a predictable place for secondary navigation and ownership information.

**Current treatment:** The landing page footer uses a raised theme-aware surface, a top divider, grouped link columns, and a compact copyright/attribution area. Links use muted text rather than competing with primary page actions.

**Replication guidance:**

- Group related destinations under clear labels.
- Keep legal, company, and product links easy to scan.
- Use enough contrast for muted links and preserve visible keyboard focus.
- Separate the footer from the closing panel without creating a jarring visual break.
- Allow columns to wrap or stack naturally on narrow screens.

### 4.11 Public subpages

**Role:** Reuse the public visual language without forcing the landing-page marketing composition onto content-heavy routes.

**Current treatment:** About, Features, and Blog use the shared public navigation and authentication dialog. A photographic banner with a dark overlay introduces the page; the main content sits in a bordered, raised surface. Feature lists and blog cards use a restrained card treatment.

**Replication guidance:**

- Reuse shared navigation, theme behavior, typography, action styles, and surface tokens.
- Use a page title treatment suited to the content; do not require every route to have a hero preview, value strip, or closing CTA.
- Prioritize readability and existing content structure.
- Keep route-specific layouts simpler than the landing page unless the content benefits from more.

### 4.12 Error and empty states

**Role:** Help visitors recover without making the application feel broken.

**Current 404 treatment:** A theme-aware, centered composition pairs a large error number and reassuring copy with a lightweight route/map illustration. It offers direct actions to Home and Features. The 403 page uses the shared public navigation.

**Ornament notes:** The 404 illustration is CSS-built, not an SVG. Its dotted background establishes texture, concentric outlines suggest a map or search area, and a broken route with nodes leads toward a small “Not here” marker. Floating card shapes connect the metaphor to project work. Keep these details grouped in a contained illustration area so they enrich the state without competing with its recovery message.

**Replication guidance:**

- Explain the state in plain language and avoid blaming the visitor.
- Offer one direct recovery action and, if useful, one secondary destination.
- Keep decorative imagery subordinate to the message.
- Preserve the actual HTTP status and application routing while changing presentation.

## 5. Quality Checklist for a Recreated Design

Before considering a recreation complete, check:

- [ ] The public pages share a coherent navigation, control, and typography system.
- [ ] The primary purpose and next action are immediately apparent.
- [ ] Light and dark themes have intentional, readable surface and text relationships.
- [ ] Buttons, links, forms, and theme controls have visible interaction and focus states.
- [ ] The hero, cards, modal, closing section, and footer reflow without overflow on small screens.
- [ ] Decorative effects do not reduce contrast, readability, or content access.
- [ ] Motion respects reduced-motion preferences and does not delay access to information.
- [ ] Existing routes, form submissions, theme persistence, and page-specific behavior still work.
- [ ] The design uses its own considered choices rather than copying the reference values mechanically.
