use std::collections::HashSet;
use crate::read_lines;

pub fn solve() {
    let input: Vec<_> = read_lines("day04.txt");

    let mut result = 0;
    let needle = "XMAS";

    let max_y = input.len() as i32;
    let max_x = input[0].len() as i32;

    // Part 1
    for y in 0..max_y {
        for x in 0..max_x {
            // Let's scan in all 8 directions at once
            for d1 in -1i32..=1i32 {
                for d2 in -1i32..=1i32 {
                    if d1 == 0 && d2 == 0 { // No direction, not interesting
                        continue;
                    }

                    let mut found = true;
                    for i in 0i32..needle.len() as i32 {
                        // I.e. (0; 1) means "scanning right"
                        // (1, 1) means "scanning down-right" etc.
                        let yk = y + i * d1;
                        let xk = x + i * d2;
                        if yk < 0 || yk >= max_y || xk < 0 || xk >= max_x {
                            found = false;
                            break;
                        }
                        let n = needle.chars().nth(i as usize).unwrap();
                        let st = input[yk as usize].chars().nth(xk as usize).unwrap();
                        if st != n {
                            found = false;
                            break;
                        }
                    }
                    if found {
                        result += 1;
                    }
                }
            }
        }
    }

    println!("{}", result);

    // Part 2
    result = 0;
    let golden = HashSet::from(['M', 'S']);
    for y in 1..(max_y as usize - 1)  {
        for x in 1..(max_x as usize - 1) {
            let curr = input[y].chars().nth(x).unwrap();
            if curr != 'A' {
                continue
            }
            let lu = input[y - 1].chars().nth(x - 1).unwrap();
            let rl = input[y + 1].chars().nth(x + 1).unwrap();

            let ll = input[y + 1].chars().nth(x - 1).unwrap();
            let ru = input[y - 1].chars().nth(x + 1).unwrap();
            if golden.eq(&HashSet::from([lu, rl])) && golden.eq(&HashSet::from([ll, ru])) {
                result += 1;
            }
        }
    }
    println!("{}", result)
}
