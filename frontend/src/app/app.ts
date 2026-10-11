import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Question } from './services/question';

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  private readonly questionService = inject(Question)
  question = '';
  answer = '';
  error = '';
  loading = false;

  askQuestion(): void {
    const question = this.question.trim();

    if (!question || this.loading) {
      return;
    }

    this.loading = true;
    this.answer = '';
    this.error = '';

    this.questionService.askQuestion(question).subscribe({
      next: (response) => {
        this.answer = response.answer;
        this.loading = false;
      },
      error: (err) => {
        if (err.status === 400) {
          this.error =
            err.error?.message ?? 'Please check your question.';
        } else if (err.status === 503) {
          this.error =
            "We couldn't generate an answer right now. Please try again later.";
        } else {
          this.error = 'Could not get an answer. Please try again.';
        }

        this.loading = false;
      }
    });
  }
}
